package com.anyi.sparrow.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.enums.PayEnterEnum;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.domain.OrderCustomerRefundPayment;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.enums.PaymentTypeEnum;
import com.anyi.common.product.domain.enums.RefundPaymentStatusEnum;
import com.anyi.common.product.domain.request.RefundPaymentApplyReq;
import com.anyi.common.product.domain.response.ReceivePaymentInfoVO;
import com.anyi.common.product.domain.response.RefundPaymentInfoVO;
import com.anyi.common.product.service.OrderCustomerRefundPaymentService;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.result.WxPayVO;
import com.anyi.common.service.AbstractRefundPaymentManage;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.service.PayApplyRecordService;
import com.anyi.common.service.ProductWxPayService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.wx.MchIdService;
import com.anyi.sparrow.applet.user.service.UserAccountProcessService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.WxUtils;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.bean.ecommerce.TransactionsResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class RefundManageService extends AbstractRefundPaymentManage {
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrderCustomerRefundPaymentService refundPaymentService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private ProductWxPayService productWxPayService;
    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MchIdService mchIdService;
    @Autowired
    private UserAccountProcessService userAccountProcessService;
    @Autowired
    private PayApplyRecordService payApplyRecordService;
    @Value("${wx.pay.customerRefundPaymentNotifyUrl}")
    private String customerRefundPaymentNotifyUrl;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    public WxPayVO pay(Long orderId, String authCode) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.WX_PAY_REFUND_PAYMENT_PAY_LOCK_KEY, orderId);
        return distributionLockUtil.lock(
                () -> payBase(orderId, authCode),
                0,
                () -> lockKey,
                "支付中",
                null);
    }

    private WxPayVO payBase(Long orderId, String authCode) {
        Order order = super.getOrder(orderId);
        OrderCustomerReceivePayment receivePayment = super.getReceivePayment(orderId);
        OrderCustomerRefundPayment refundPayment = super.getRefundPayment(order.getId(), receivePayment.getId());
        // 根据authCode获取用户openId
        UserAccount ua = userAccountProcessService.loginByCode(authCode);

        // 异步记录申请日志
        payApplyRecordService.addRecord(orderId,
                ua.getId(),
                PayEnterEnum.CUSTOMER_REFUND_PAYMENT.getUrl(),
                refundPayment.getOutTradeNo(),
                mchIdService.getJxzSubMchId(),
                PayEnterEnum.CUSTOMER_REFUND_PAYMENT.getBizType());

        OrderCustomerRefundPayment newPayment = preQueryPayOrderStatus(refundPayment);
        PartnerTransactionsRequest payReq = buildPayRequest(newPayment, ua.getOpenId());
        TransactionsResult.JsapiResult res = productWxPayService.partnerTransactions(payReq);
        return BeanUtil.copyProperties(res, WxPayVO.class);
    }

    /**
     * 查询支付订单状态，成功支付抛出；其他情况关闭订单重新生成商户订单号
     */
    private OrderCustomerRefundPayment preQueryPayOrderStatus(OrderCustomerRefundPayment payment) {
        String oldOtn = payment.getOutTradeNo();
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsQueryRequest req = super.buildPayQueryReq(oldOtn, wxPayService);

        PartnerTransactionsResult res;
        try {
            res = productWxPayService.queryPartnerTransactions(req);
        } catch (BaseException e) {
            log.info("RefundManageService.preQueryPayOrderStatus.error: {}", e.getMessage());
            return payment;
        }

        boolean payed = WxPayConstants.WxpayTradeStatus.SUCCESS.equals(res.getTradeState());

        if (payed) {
            log.info("RefundManageService.preQueryPayOrderStatus.info: 订单已支付");
            throw new BaseException(-1, "订单已支付");
        }
        productWxPayService.closePartnerTransactions(payment.getOutTradeNo());
        String newOtn = super.generateOutTradeNo();
        payment.setOutTradeNo(newOtn);
        refundPaymentService.lambdaUpdate()
                .set(OrderCustomerRefundPayment::getOutTradeNo, newOtn)
                .eq(OrderCustomerRefundPayment::getId, payment.getId())
                .update(new OrderCustomerRefundPayment());
        log.info("RefundManageService.preQueryPayOrderStatus.info: 订单查询状态为-{}, 关闭支付订单, 重新生成订单号, oldOtn-{}, newOtn-{}",
                res.getTradeStateDesc(), oldOtn, newOtn);
        return payment;
    }

    private PartnerTransactionsRequest buildPayRequest(OrderCustomerRefundPayment payment, String openId) {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsRequest.Amount amount = new PartnerTransactionsRequest.Amount();
        amount.setCurrency(WxPayConstants.CurrencyType.CNY);
        amount.setTotal(payment.getAmount());
        //if (anyiConfig.isTestEnable()) {
        //    amount.setTotal(1);
        //}
        PartnerTransactionsRequest.Payer payer = new PartnerTransactionsRequest.Payer();
        payer.setSubOpenid(openId);
        return PartnerTransactionsRequest.builder()
                .spAppid(wxPayService.getConfig().getAppId())
                .spMchid(wxPayService.getConfig().getMchId())
                .subAppid(wxPayService.getConfig().getSubAppId())
                .subMchid(wxPayService.getConfig().getSubMchId())
                .notifyUrl(customerRefundPaymentNotifyUrl)
                .description(dictService.getRefundPaymentDescription())
                .outTradeNo(payment.getOutTradeNo())
                .amount(amount)
                .payer(payer)
                // 30分钟后失效
                //.timeExpire(CommonWxUtils.getTimeExpireV3(payment.getCreateTime(), 30))
                .build();
    }

    @Override
    protected Map<Long, Employee> getEmployeeInfoMap(ReceivePaymentInfoVO vo) {
        return employeeService.getEmployeeInfoMap(Stream.of(vo.getStoreEmployeeId(), vo.getRecyclerEmployeeId()).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    @Override
    protected Map<Long, Company> getCompanyInfoMap(ReceivePaymentInfoVO vo) {
        return companyService.getCompanyInfoMap(Stream.of(vo.getStoreCompanyId(), vo.getRecyclerCompanyId()).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    @Override
    protected void setReceivePaymentService(ReceivePaymentInfoVO vo, OrderCustomerReceivePayment payment) {
        vo.setReceivePaymentInfo(payment);
    }

    @Transactional(rollbackFor = Exception.class)
    public RefundPaymentInfoVO apply(RefundPaymentApplyReq req) {
        // 获取订单信息
        Order order = super.getOrder(req.getId());
        // 收款信息
        OrderCustomerReceivePayment receivePayment = this.getReceivePayment(req.getId());
        OrderCustomerRefundPayment refundPayment = getRefundPayment(req.getId(), receivePayment.getId());
        if (refundPayment != null) {
            return BeanUtil.copyProperties(refundPayment, RefundPaymentInfoVO.class);
        }

        String outTradeNo = super.generateOutTradeNo();
        // 生成小程序二维码
        Map<String, String> params = Collections.singletonMap("id", order.getId().toString());
        String qrCodeUrl = wxUtils.genQrCode("pages/refund/index?", params, 60);
        OrderCustomerRefundPayment payment = OrderCustomerRefundPayment.builder()
                .id(snowflakeIdService.nextId())
                .orderId(order.getId())
                .receivePaymentId(receivePayment.getId())
                .outTradeNo(outTradeNo)
                .amount(receivePayment.getAmount())
                .status(RefundPaymentStatusEnum.PENDING_PAYMENT.getCode())
                .type(PaymentTypeEnum.WECHAT.getCode())
                .reason(req.getReason())
                .qrCodeUrl(qrCodeUrl)
                .build();
        boolean success = refundPaymentService.save(payment);
        if (success) {
            orderLogService.addLog(LoginUserContext.getUserIdByCatch(),
                    order.getId(),
                    OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.APPLY_REFUND_INFO.getCode(),
                    OrderOperationEnum.APPLY_REFUND_INFO.getDesc(),
                    OrderOperationEnum.APPLY_REFUND_INFO.getRemark());
        }

        return BeanUtil.copyProperties(payment, RefundPaymentInfoVO.class);
    }
}