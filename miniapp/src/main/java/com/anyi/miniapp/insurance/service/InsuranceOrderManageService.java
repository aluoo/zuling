package com.anyi.miniapp.insurance.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.domain.DiInsuranceOrder;
import com.anyi.common.insurance.domain.DiInsuranceOrderPayment;
import com.anyi.common.insurance.domain.dto.DiOrderPaymentUpdateDTO;
import com.anyi.common.insurance.enums.DiOrderPayStatusEnum;
import com.anyi.common.insurance.enums.DiOrderStatusEnum;
import com.anyi.common.insurance.enums.DiOrderSubStatusEnum;
import com.anyi.common.insurance.req.InsuranceOrderPaymentInfoVO;
import com.anyi.common.insurance.service.DiInsuranceOrderPaymentService;
import com.anyi.common.insurance.service.DiInsuranceOrderService;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.RefundPaymentStatusEnum;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.service.ProductWxPayService;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.util.MoneyUtil;
import com.anyi.common.wx.CommonWxUtils;
import com.anyi.common.wx.MchIdService;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsNotifyResult;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/11
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class InsuranceOrderManageService {
    @Autowired
    private DiInsuranceOrderService insuranceOrderService;
    @Autowired
    private DiInsuranceOrderPaymentService insuranceOrderPaymentService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MchIdService mchIdService;
    @Autowired
    private ProductWxPayService productWxPayService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;
    @Autowired
    private OrderLogService orderLogService;

    public InsuranceOrderPaymentInfoVO getOrderInfo(Long orderId) {
        DiInsuranceOrder order = Optional.ofNullable(insuranceOrderService.lambdaQuery()
                .eq(DiInsuranceOrder::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, order.getId())
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));

        return InsuranceOrderPaymentInfoVO.builder()
                .amount(order.getPrice())
                .amountStr(MoneyUtil.fenToYuan(order.getPrice()))
                .payTime(payment.getPayTime())
                .status(payment.getStatus())
                .productName(order.getProductName())
                .productSpec(order.getProductSpec())
                .insuranceName(order.getInsuranceName())
                .insuranceType(order.getInsuranceType())
                .insurancePeriod(order.getInsurancePeriod())
                .qrCodeUrl(payment.getQrCodeUrl())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean payQuery(Long orderId) {
        DiInsuranceOrderPayment payment = Optional.ofNullable(insuranceOrderPaymentService.lambdaQuery()
                .eq(DiInsuranceOrderPayment::getInsuranceOrderId, orderId)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
        if (payment.getStatus().equals(DiOrderPayStatusEnum.PAYED.getCode())) {
            log.info("payQuery.info: 订单已支付");
            return true;
        }
        // query
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsQueryRequest queryReq = buildPayQueryReq(payment.getOutTradeNo(), wxPayService);
        PartnerTransactionsResult res;
        try {
            res = productWxPayService.queryPartnerTransactions(queryReq);
        } catch (BaseException e) {
            return false;
        }
        if (res == null) {
            log.info("payOrderQuery.info: queryPartnerTransactions-res is null");
            return false;
        }
        boolean payed = WxPayConstants.WxpayTradeStatus.SUCCESS.equals(res.getTradeState());
        if (!payed) {
            log.info("payOrderQuery.info: 微信订单查询状态为-{}", res.getTradeStateDesc());
            return false;
        }
        log.info("payOrderQuery.info: 微信订单查询状态为-{}-{}", res.getTradeState(), res.getTradeStateDesc());

        DiOrderPaymentUpdateDTO dto = new DiOrderPaymentUpdateDTO(res);
        paySuccess(dto);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void payNotify(String notify) {
        PartnerTransactionsNotifyResult notifyResult = productWxPayService.parseNotify(notify, mchIdService.getJxzSubMchId());

        DiOrderPaymentUpdateDTO dto = new DiOrderPaymentUpdateDTO(notifyResult);
        paySuccess(dto);
    }

    private void paySuccess(DiOrderPaymentUpdateDTO req) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.WX_PAY_DI_PAYMENT_PAY_SUCCESS_LOCK_KEY, req.getOutTradeNo());
        distributionLockUtil.lock(
                () -> paySuccessBase(req),
                0,
                () -> lockKey,
                "支付中",
                null);
    }

    private void paySuccessBase(DiOrderPaymentUpdateDTO req) {
        log.info("paySuccess.info: req-{}", JSONUtil.toJsonStr(req));
        DiInsuranceOrderPayment payment = insuranceOrderPaymentService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(DiInsuranceOrderPayment::getOutTradeNo, req.getOutTradeNo())
                .one();
        if (payment == null) {
            log.info("paySuccess.info: 没有找到otn-{}对应的订单信息", req.getOutTradeNo());
            return;
        }
        if (payment.getStatus().equals(RefundPaymentStatusEnum.PAYED.getCode())) {
            log.info("paySuccess.info: 订单已支付");
            return;
        }
        boolean payed = WxPayConstants.WxpayTradeStatus.SUCCESS.equals(req.getTradeState());
        if (!payed) {
            log.info("paySuccess.info: pay failed. otn-{}, {}-{}", req.getOutTradeNo(), req.getTradeState(), req.getTradeStateDesc());
            return;
        }
        // update payment
        boolean success = insuranceOrderPaymentService.lambdaUpdate()
                .set(DiInsuranceOrderPayment::getOpenId, req.getOpenId())
                .set(DiInsuranceOrderPayment::getTransactionId, req.getTransactionId())
                .set(DiInsuranceOrderPayment::getPayTime, CommonWxUtils.toDateV3(req.getSuccessTime()))
                .set(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PAYED.getCode())
                .eq(DiInsuranceOrderPayment::getStatus, DiOrderPayStatusEnum.PENDING_PAYMENT.getCode())
                .eq(DiInsuranceOrderPayment::getId, payment.getId())
                .update(new DiInsuranceOrderPayment());
        if (!success) {
            return;
        }
        success = insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_AUDIT.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_AUDIT_ORDER.getCode())
                .eq(DiInsuranceOrder::getId, payment.getInsuranceOrderId())
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_PAYMENT.getCode())
                .update(new DiInsuranceOrder());
        if (success) {
            // log
            orderLogService.addLog(
                    null,
                    payment.getInsuranceOrderId(),
                    DiOrderStatusEnum.PENDING_AUDIT.getCode(),
                    OrderOperationEnum.DI_ORDER_PAY.getCode(),
                    OrderOperationEnum.DI_ORDER_PAY.getDesc(),
                    OrderOperationEnum.DI_ORDER_PAY.getRemark()
            );
        }
    }

    protected PartnerTransactionsQueryRequest buildPayQueryReq(String outTradeNo, WxPayService wxPayService) {
        PartnerTransactionsQueryRequest req = new PartnerTransactionsQueryRequest();
        req.setOutTradeNo(outTradeNo);
        req.setSpMchid(wxPayService.getConfig().getMchId());
        req.setSubMchid(wxPayService.getConfig().getSubMchId());
        return req;
    }
}