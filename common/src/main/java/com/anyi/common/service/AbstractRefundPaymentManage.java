package com.anyi.common.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.domain.OrderCustomerRefundPayment;
import com.anyi.common.product.domain.OrderQuotePriceLog;
import com.anyi.common.product.domain.dto.ProductDTO;
import com.anyi.common.product.domain.dto.RefundPaymentUpdateDTO;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogStatusEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.enums.RefundPaymentStatusEnum;
import com.anyi.common.product.domain.response.ReceivePaymentInfoVO;
import com.anyi.common.product.domain.response.RefundPaymentInfoVO;
import com.anyi.common.product.service.*;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.wx.CommonWxUtils;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class AbstractRefundPaymentManage {
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected OrderCustomerReceivePaymentService receivePaymentService;
    @Autowired
    protected OrderCustomerRefundPaymentService refundPaymentService;
    @Autowired
    protected CompanyService companyService;
    @Autowired
    protected EmployeeService employeeService;
    @Autowired
    protected OrderOptionService orderOptionService;
    @Autowired
    protected OrderLogService orderLogService;
    @Autowired
    protected ProductService productService;
    @Autowired
    private OrderQuotePriceLogService orderQuotePriceLogService;
    @Autowired
    private CommissionProcessService commissionProcessService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    public RefundPaymentInfoVO getRefundPaymentInfo(Long orderId) {
        // 获取订单信息
        Order order = this.getOrder(orderId);
        // 收款信息
        OrderCustomerReceivePayment receivePayment = this.getReceivePayment(orderId);
        // 退款信息
        OrderCustomerRefundPayment refundPayment = this.getRefundPayment(orderId, receivePayment.getId());
        ReceivePaymentInfoVO orderBase = this.getReceivePaymentInfo(order, receivePayment);
        if (refundPayment == null) {
            // 不存在，可申请，返回
            return RefundPaymentInfoVO.builder()
                    .order(orderBase)
                    .build();
        }

        // 已支付或未支付，都直接返回
        RefundPaymentInfoVO vo = BeanUtil.copyProperties(refundPayment, RefundPaymentInfoVO.class);
        vo.setOrder(orderBase);
        vo.setAmountStr();
        return vo;
    }

    protected Order getOrder(Long orderId) {
        if (orderId == null) {
            throw new BaseException(BizError.MB_ORDER_ID_NULL);
        }
        Order order = Optional.ofNullable(orderService.lambdaQuery()
                .eq(Order::getId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));

        if (order.getStatus().equals(OrderStatusEnum.REFUNDED.getCode())) {
            throw new BaseException(-1, "订单已退款");
        }

        if (!order.getStatus().equals(OrderStatusEnum.PENDING_SHIPMENT.getCode())) {
            throw new BaseException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }

        return order;
    }

    protected OrderCustomerReceivePayment getReceivePayment(Long orderId) {
        if (orderId == null) {
            throw new BaseException(BizError.MB_ORDER_ID_NULL);
        }
        return Optional.ofNullable(receivePaymentService.lambdaQuery()
                .eq(OrderCustomerReceivePayment::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
    }

    protected OrderCustomerRefundPayment getRefundPayment(Long orderId, Long receivePaymentId) {
        if (orderId == null) {
            throw new BaseException(BizError.MB_ORDER_ID_NULL);
        }
        return refundPaymentService.lambdaQuery()
                .eq(OrderCustomerRefundPayment::getOrderId, orderId)
                .eq(OrderCustomerRefundPayment::getReceivePaymentId, receivePaymentId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
    }

    protected OrderCustomerRefundPayment getRefundPayment(Long orderId) {
        if (orderId == null) {
            throw new BaseException(BizError.MB_ORDER_ID_NULL);
        }
        return Optional.ofNullable(refundPaymentService.lambdaQuery()
                .eq(OrderCustomerRefundPayment::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));
    }

    protected ReceivePaymentInfoVO getReceivePaymentInfo(Order order, OrderCustomerReceivePayment payment) {
        ReceivePaymentInfoVO vo = ReceivePaymentInfoVO.builder().build();
        BeanUtil.copyProperties(order, vo);
        vo.setAmount(vo.getFinalPrice());
        vo.setAmountStr();
        Map<Long, String> orderSpecInfoMap = orderOptionService.buildOrderSpecInfoMap(Collections.singletonList(order.getId()));
        // 设置规格信息
        vo.setSpec(orderSpecInfoMap.get(order.getId()));
        // 设置门店信息
        Map<Long, Employee> employeeInfoMap = this.getEmployeeInfoMap(vo);
        Map<Long, Company> companyInfoMap = this.getCompanyInfoMap(vo);
        vo.setCompanyInfo(companyInfoMap);
        vo.setEmployeeInfo(employeeInfoMap);
        Map<Long, ProductDTO> productInfoMap = productService.getProductInfoMap(Collections.singletonList(vo.getProductId()));
        // 设置品牌logo
        vo.setBrandLogo(Optional.ofNullable(productInfoMap.get(vo.getProductId())).map(ProductDTO::getBrandLogo).orElse(null));
        this.setReceivePaymentService(vo, payment);
        return vo;
    }

    protected void paySuccess(RefundPaymentUpdateDTO req) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.WX_PAY_REFUND_PAYMENT_PAY_SUCCESS_LOCK_KEY, req.getOutTradeNo());
        distributionLockUtil.lock(
                () -> paySuccessBase(req),
                0,
                () -> lockKey,
                "支付中",
                null);
    }

    private void paySuccessBase(RefundPaymentUpdateDTO req) {
        log.info("paySuccess.info: req-{}", JSONUtil.toJsonStr(req));

        OrderCustomerRefundPayment payment = refundPaymentService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(OrderCustomerRefundPayment::getOutTradeNo, req.getOutTradeNo())
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
        boolean success = refundPaymentService.lambdaUpdate()
                .set(OrderCustomerRefundPayment::getOpenId, req.getOpenId())
                .set(OrderCustomerRefundPayment::getTransactionId, req.getTransactionId())
                .set(OrderCustomerRefundPayment::getPayTime, CommonWxUtils.toDateV3(req.getSuccessTime()))
                .set(OrderCustomerRefundPayment::getStatus, RefundPaymentStatusEnum.PAYED.getCode())
                .eq(OrderCustomerRefundPayment::getId, payment.getId())
                .eq(OrderCustomerRefundPayment::getStatus, RefundPaymentStatusEnum.PENDING_PAYMENT.getCode())
                .update(new OrderCustomerRefundPayment());
        if (!success) {
            return;
        }
        // update order
        success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.REFUNDED.getCode())
                .eq(Order::getId, payment.getOrderId())
                .eq(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .update(new Order());
        if (success) {
            // 成功支付，回滚资金流水
            Order order = orderService.lambdaQuery().eq(Order::getId, payment.getOrderId()).one();
            OrderQuotePriceLog confirmedQuotePriceLog = orderQuotePriceLogService.getById(order.getQuotePriceLogId());
            commissionProcessService.rollbackRecyclerAccount(order.getRecyclerCompanyId(), confirmedQuotePriceLog, order.getProductName());
            // 回滚待结算佣金记录
            commissionProcessService.cancelWaitSettle(order.getId(), order.getProductName());
            // 将已确认交易的报价记录改为已作废/交易退款
            orderQuotePriceLogService.refundTrade(confirmedQuotePriceLog.getId());
            orderLogService.addLog(null,
                    confirmedQuotePriceLog.getId(),
                    OrderQuoteLogStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.QUOTE_CANCELED_REFUND_TRADE.getCode(),
                    OrderOperationEnum.QUOTE_CANCELED_REFUND_TRADE.getDesc(),
                    OrderOperationEnum.QUOTE_CANCELED_REFUND_TRADE.getRemark());
            // 日志
            orderLogService.addLog(null,
                    payment.getOrderId(),
                    OrderStatusEnum.REFUNDED.getCode(),
                    OrderOperationEnum.REFUND.getCode(),
                    OrderOperationEnum.REFUND.getDesc(),
                    OrderOperationEnum.REFUND.getRemark());
        }
    }

    protected PartnerTransactionsQueryRequest buildPayQueryReq(String outTradeNo, WxPayService wxPayService) {
        PartnerTransactionsQueryRequest req = new PartnerTransactionsQueryRequest();
        req.setOutTradeNo(outTradeNo);
        req.setSpMchid(wxPayService.getConfig().getMchId());
        req.setSubMchid(wxPayService.getConfig().getSubMchId());
        return req;
    }

    protected String generateOutTradeNo() {
        return CommonWxUtils.createOrderNo();
    }

    protected abstract Map<Long, Employee> getEmployeeInfoMap(ReceivePaymentInfoVO vo);
    protected abstract Map<Long, Company> getCompanyInfoMap(ReceivePaymentInfoVO vo);

    protected abstract void setReceivePaymentService(ReceivePaymentInfoVO vo, OrderCustomerReceivePayment payment);
}