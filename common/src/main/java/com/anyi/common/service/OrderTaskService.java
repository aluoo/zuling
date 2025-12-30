package com.anyi.common.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.domain.DiInsuranceOrder;
import com.anyi.common.insurance.domain.DiInsuranceOrderPayment;
import com.anyi.common.insurance.enums.DiOrderInsuranceStatusEnum;
import com.anyi.common.insurance.enums.DiOrderStatusEnum;
import com.anyi.common.insurance.enums.DiOrderSubStatusEnum;
import com.anyi.common.insurance.service.DiInsuranceOrderPaymentService;
import com.anyi.common.insurance.service.DiInsuranceOrderService;
import com.anyi.common.product.domain.*;
import com.anyi.common.product.domain.enums.*;
import com.anyi.common.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/28
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderTaskService {
    @Autowired
    CommonSysDictService commonSysDictService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderQuotePriceLogService orderQuotePriceLogService;
    @Autowired
    OrderCustomerReceivePaymentService receivePaymentService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    ShippingOrderRelService shippingOrderRelService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    DiInsuranceOrderService insuranceOrderService;
    @Autowired
    DiInsuranceOrderPaymentService insuranceOrderPaymentService;
    @Autowired
    CommissionSettleService commissionSettleService;

    @Transactional(rollbackFor = Exception.class)
    public void autoUploadInsuranceOrder() {
        // 审核成功七天后自动上传
        log.info("autoUploadInsuranceOrder.start");
        int insuranceOrderAutoUploadDay = commonSysDictService.getInsuranceOrderAutoUploadDay();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.endOfDay(DateUtil.offset(now, DateField.DAY_OF_YEAR, -insuranceOrderAutoUploadDay));
        log.info("autoUploadInsuranceOrder.info: now-{}, auto upload day-{}, target time-{}", now, insuranceOrderAutoUploadDay, offset);

        // 找出所有待上传的订单列表
        List<DiInsuranceOrder> list = insuranceOrderService.lambdaQuery()
                .select(DiInsuranceOrder::getId)
                .eq(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_UPLOAD_INSURANCE.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("autoUploadInsuranceOrder.end: no available order");
            return;
        }
        List<Long> orderIds = list.stream().map(DiInsuranceOrder::getId).collect(Collectors.toList());
        List<OrderLog> logs = orderLogService.lambdaQuery()
                .eq(OrderLog::getDeleted, false)
                .eq(OrderLog::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                .eq(OrderLog::getOperationStatus, OrderOperationEnum.DI_ORDER_AUDIT_PASSED.getCode())
                .le(OrderLog::getCreateTime, offset)
                .in(OrderLog::getOrderId, orderIds)
                .list();
        if (CollUtil.isEmpty(logs)) {
            log.info("autoUploadInsuranceOrder.end: no available order");
            return;
        }

        for (OrderLog ol : logs) {
            // 更新订单为已完成-待出保
            insuranceOrderService.lambdaUpdate()
                    .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_EFFECTIVE.getCode())
                    .set(DiInsuranceOrder::getInsuranceStatus, DiOrderInsuranceStatusEnum.PENDING_EFFECTIVE.getCode())
                    .eq(DiInsuranceOrder::getId, ol.getOrderId())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new DiInsuranceOrder());
            orderLogService.addLog(
                    null,
                    ol.getOrderId(),
                    DiOrderStatusEnum.FINISHED.getCode(),
                    OrderOperationEnum.DI_ORDER_UPLOAD_PASSED.getCode(),
                    OrderOperationEnum.DI_ORDER_UPLOAD_PASSED.getDesc(),
                    OrderOperationEnum.DI_ORDER_UPLOAD_PASSED.getRemark()
            );
            // 结算佣金
            commissionSettleService.settleOrder(ol.getOrderId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_SERVICE.getType(), EmployAccountChangeEnum.insurance_commission, EmployAccountChangeEnum.insurance_commission.getRemark());
            commissionSettleService.settleOrder(ol.getOrderId(), CommissionBizType.INSURANCE_SERVICE, CommissionPackage.INSURANCE_COMPANY_SERVICE.getType(), EmployAccountChangeEnum.insurance_commission, EmployAccountChangeEnum.insurance_commission.getRemark());
        }

        log.info("autoUploadInsuranceOrder.end: upload {} orders", orderIds.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueInsuranceOrder() {
        log.info("autoCloseOverdueInsuranceOrder.start");
        int insuranceOrderExpiredMinutes = commonSysDictService.getInsuranceOrderExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -insuranceOrderExpiredMinutes);
        log.info("autoCloseOverdueInsuranceOrder.info: now-{}, over expired min-{}, overdue time-{}", now, insuranceOrderExpiredMinutes, offset);

        // 找出已过期的订单列表
        List<DiInsuranceOrder> list = insuranceOrderService.lambdaQuery()
                .select(DiInsuranceOrder::getId)
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_PAYMENT.getCode())
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("autoCloseOverdueInsuranceOrder.end: no available order");
            return;
        }
        // 批量更新 关闭订单
        List<Long> orderIds = list.stream().map(DiInsuranceOrder::getId).collect(Collectors.toList());
        insuranceOrderService.lambdaUpdate()
                .set(DiInsuranceOrder::getStatus, DiOrderStatusEnum.CANCELED.getCode())
                .set(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.AUTO_CANCELED.getCode())
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_PAYMENT.getCode())
                .in(DiInsuranceOrder::getId, orderIds)
                .update(new DiInsuranceOrder());
        // 批量日志
        orderLogService.addLogBatch(null,
                orderIds,
                OrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.DI_ORDER_CANCEL_AUTO.getCode(),
                OrderOperationEnum.DI_ORDER_CANCEL_AUTO.getDesc(),
                OrderOperationEnum.DI_ORDER_CANCEL_AUTO.getRemark());
        log.info("autoCloseOverdueInsuranceOrder.end: closed {} orders", orderIds.size());

        insuranceOrderPaymentService.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .in(DiInsuranceOrderPayment::getInsuranceOrderId, orderIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new DiInsuranceOrderPayment());

        log.info("autoCloseOverdueInsuranceOrder.end: closed {} orders", orderIds.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void repairQuoteSpentTimeReal() {
        List<OrderQuotePriceLog> list = orderQuotePriceLogService.lambdaQuery()
                .eq(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.QUOTED.getCode())
                .isNull(OrderQuotePriceLog::getQuoteTimeSpentReal)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("repairQuoteSpentTimeReal.end: no data");
            return;
        }
        list.forEach(ql -> {
            Long employeeId = ql.getEmployeeId();
            Long orderId = ql.getId();
            OrderLog lockQuoteLog = orderLogService.lambdaQuery()
                    .eq(OrderLog::getCreateBy, employeeId)
                    .eq(OrderLog::getOrderId, orderId)
                    .eq(OrderLog::getOperationStatus, OrderOperationEnum.LOCK_QUOTE.getCode())
                    .orderByDesc(OrderLog::getCreateTime)
                    .last("limit 1")
                    .one();
            if (log == null) {
                return;
            }
            long quoteTimeSpentReal = DateUtil.between(lockQuoteLog.getCreateTime(), ql.getQuoteTime(), DateUnit.MS);
            orderQuotePriceLogService.lambdaUpdate()
                    .set(OrderQuotePriceLog::getQuoteTimeSpentReal, quoteTimeSpentReal)
                    .eq(OrderQuotePriceLog::getId, ql.getId())
                    .update();
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelReceivePayment() {
        int alipayReceivePaymentExpiredMinutes = commonSysDictService.getAlipayReceivePaymentExpiredMinutes();
        if (alipayReceivePaymentExpiredMinutes <= 0) {
            log.info("cancelReceivePayment.end: no expired setting");
            return;
        }
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -alipayReceivePaymentExpiredMinutes);
        log.info("cancelReceivePayment.info: now-{}, over expired min-{}, overdue time-{}", now, alipayReceivePaymentExpiredMinutes, offset);

        List<OrderCustomerReceivePayment> list = receivePaymentService.lambdaQuery()
                .select(OrderCustomerReceivePayment::getId, OrderCustomerReceivePayment::getOrderId)
                .eq(OrderCustomerReceivePayment::getStatus, ReceivePaymentStatusEnum.PENDING_PAYMENT.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .le(AbstractBaseEntity::getCreateTime, offset)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("cancelReceivePayment.end: no available order");
            return;
        }
        List<Long> paymentIds = list.stream().map(OrderCustomerReceivePayment::getId).collect(Collectors.toList());
        // 批量更新收款单状态
        boolean success = receivePaymentService.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .in(OrderCustomerReceivePayment::getId, paymentIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(OrderCustomerReceivePayment::getStatus, ReceivePaymentStatusEnum.PENDING_PAYMENT.getCode())
                .update(new OrderCustomerReceivePayment());
        List<Long> orderIds = list.stream().map(OrderCustomerReceivePayment::getOrderId).collect(Collectors.toList());
        // 批量插入日志
        if (success) {
            orderLogService.addLogBatch(
                    null,
                    orderIds,
                    OrderStatusEnum.PENDING_PAYMENT.getCode(),
                    OrderOperationEnum.AUTO_CLOSE_RECEIVE_PAYMENT.getCode(),
                    OrderOperationEnum.AUTO_CLOSE_RECEIVE_PAYMENT.getDesc(),
                    OrderOperationEnum.AUTO_CLOSE_RECEIVE_PAYMENT.getRemark());
        }
        log.info("cancelReceivePayment.end: closed {} payments", list.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueOrder() {
        log.info("autoCloseOverdueOrder.start");
        int productOrderExpiredMinutes = commonSysDictService.getProductOrderExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -productOrderExpiredMinutes);
        log.info("autoCloseOverdueOrder.info: now-{}, over expired min-{}, overdue time-{}", now, productOrderExpiredMinutes, offset);

        // 找出已过期的订单列表
        List<Order> list = orderService.lambdaQuery()
                .select(Order::getId)
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("autoCloseOverdueOrder.end: no available order");
            return;
        }
        // 批量更新 关闭订单
        List<Long> orderIds = list.stream().map(Order::getId).collect(Collectors.toList());
        orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.CANCELED.getCode())
                .set(Order::getQuotable, false)
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .in(Order::getId, orderIds)
                .update(new Order());
        // 批量日志
        orderLogService.addLogBatch(null,
                orderIds,
                OrderStatusEnum.CANCELED.getCode(),
                OrderOperationEnum.AUTO_CLOSE.getCode(),
                OrderOperationEnum.AUTO_CLOSE.getDesc(),
                OrderOperationEnum.AUTO_CLOSE.getRemark());
        log.info("autoCloseOverdueOrder.end: closed {} orders", orderIds.size());

        // 找出关联的报价记录
        List<OrderQuotePriceLog> quoteLogs = orderQuotePriceLogService.lambdaQuery()
                .in(OrderQuotePriceLog::getOrderId, orderIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(quoteLogs)) {
            log.info("autoCloseOverdueOrder.end: no available quote log");
            return;
        }
        // 未报价的更新为报价超时
        // 插入已作废-超时未报价日志
        List<Long> notQuotedLogIds = quoteLogs.stream()
                .filter(o -> o.getQuoted().equals(false) && Arrays.asList(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode(), OrderQuoteLogStatusEnum.QUOTING.getCode()).contains(o.getStatus()))
                .map(OrderQuotePriceLog::getId)
                .collect(Collectors.toList());
        updateQuoteCanceled(notQuotedLogIds, OrderQuoteLogSubStatusEnum.QUOTE_OVERDUE, OrderOperationEnum.QUOTE_CANCELED_OVERDUE);

        // 已报价的更新为门店确认交易超时
        List<Long> quotedLogIds = quoteLogs.stream()
                .filter(o -> o.getQuoted().equals(true) && o.getStatus().equals(OrderQuoteLogStatusEnum.QUOTED.getCode()) && o.getSubStatus().equals(OrderQuoteLogSubStatusEnum.QUOTING.getCode()))
                .map(OrderQuotePriceLog::getId)
                .collect(Collectors.toList());
        updateQuoteCanceled(quotedLogIds, OrderQuoteLogSubStatusEnum.CONFIRM_OVERDUE, OrderOperationEnum.QUOTE_CANCELED_CONFIRM_OVERDUE);


        log.info("autoCloseOverdueOrder.end: updated quoteOverdue-{}, confirmOverdue-{}", notQuotedLogIds.size(), quotedLogIds.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueOrderQuote() {
        log.info("autoCloseOverdueOrderQuote.start");
        int quoteExpiredMinutes = commonSysDictService.getQuoteExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -quoteExpiredMinutes);
        log.info("autoCloseOverdueOrderQuote.info: now-{}, over expired min-{}, overdue time-{}", now, quoteExpiredMinutes, offset);

        // 找出已过期的订单列表
        List<Order> list = orderService.lambdaQuery()
                .select(Order::getId)
                .eq(Order::getQuotable, true)
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(list)) {
            log.info("autoCloseOverdueOrderQuote.end: no available order");
            return;
        }
        // 批量更新 关闭报价功能
        List<Long> orderIds = list.stream().map(Order::getId).collect(Collectors.toList());
        orderService.lambdaUpdate()
                .set(Order::getQuotable, false)
                .in(Order::getId, orderIds)
                .update(new Order());
        // 批量日志
        orderLogService.addLogBatch(null,
                orderIds,
                OrderStatusEnum.UNCHECKED.getCode(),
                OrderOperationEnum.AUTO_CLOSE_QUOTE.getCode(),
                OrderOperationEnum.AUTO_CLOSE_QUOTE.getDesc(),
                OrderOperationEnum.AUTO_CLOSE_QUOTE.getRemark());
        log.info("autoCloseOverdueOrderQuote.end: closed {} orders quote function", orderIds.size());

        // 找出没有报价的订单，关闭
        Map<Long, Integer> orderQuoteCountMap = orderQuotePriceLogService.countGroupByOrderIds(orderIds);
        List<Long> needCloseOrderIds = orderQuoteCountMap.entrySet()
                .stream()
                .filter(o -> o.getValue().equals(0)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needCloseOrderIds)) {
            orderService.lambdaUpdate()
                    .set(Order::getStatus, OrderStatusEnum.CANCELED.getCode())
                    .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                    .in(Order::getId, needCloseOrderIds)
                    .update(new Order());
            // 批量日志
            orderLogService.addLogBatch(null,
                    orderIds,
                    OrderStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.AUTO_CLOSE_ORDER_WHEN_NO_QUOTE.getCode(),
                    OrderOperationEnum.AUTO_CLOSE_ORDER_WHEN_NO_QUOTE.getDesc(),
                    OrderOperationEnum.AUTO_CLOSE_ORDER_WHEN_NO_QUOTE.getRemark());
            log.info("autoCloseOverdueOrderQuote.info: closed {} orders", orderIds.size());
        }

        // 找出关联的未报价报价记录
        List<OrderQuotePriceLog> quoteLogs = orderQuotePriceLogService.lambdaQuery()
                .in(OrderQuotePriceLog::getOrderId, orderIds)
                .eq(OrderQuotePriceLog::getQuoted, false)
                .eq(AbstractBaseEntity::getDeleted, false)
                .in(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode(), OrderQuoteLogStatusEnum.QUOTING.getCode())
                .list();
        if (CollUtil.isEmpty(quoteLogs)) {
            log.info("autoCloseOverdueOrderQuote.end: no available quote log");
            return;
        }
        // 未报价的更新为已作废-超时未报价
        List<Long> notQuotedLogIds = quoteLogs.stream().map(OrderQuotePriceLog::getId).collect(Collectors.toList());
        updateQuoteCanceled(notQuotedLogIds, OrderQuoteLogSubStatusEnum.QUOTE_OVERDUE, OrderOperationEnum.QUOTE_CANCELED_OVERDUE);
        log.info("autoCloseOverdueOrderQuote.end: updated quoteOverdue-{}", notQuotedLogIds.size());
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOverdueShippingOrder() {
        log.info("autoCloseOverdueShippingOrder.start");
        int shippingOrderExpiredMinutes = commonSysDictService.getShippingOrderExpiredMinutes();
        DateTime now = DateUtil.date();
        DateTime offset = DateUtil.offset(now, DateField.MINUTE, -shippingOrderExpiredMinutes);
        log.info("autoCloseOverdueShippingOrder.info: now-{}, over expired min-{}, overdue time-{}", now, shippingOrderExpiredMinutes, offset);
        // 找出超时的发货订单
        List<ShippingOrder> shippingOrders = shippingOrderService.lambdaQuery()
                .select(ShippingOrder::getId, ShippingOrder::getStatus)
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .le(AbstractBaseEntity::getCreateTime, offset)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        if (CollUtil.isEmpty(shippingOrders)) {
            log.info("autoCloseOverdueShippingOrder.end: no available shipping order");
            return;
        }
        // 更新为已取消
        List<Long> shippingOrderIds = shippingOrders.stream().map(ShippingOrder::getId).collect(Collectors.toList());
        boolean success = shippingOrderService.lambdaUpdate()
                .set(ShippingOrder::getStatus, ShippingOrderStatusEnum.CANCELED.getCode())
                .eq(ShippingOrder::getStatus, ShippingOrderStatusEnum.PENDING_SHIPMENT.getCode())
                .in(ShippingOrder::getId, shippingOrderIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new ShippingOrder());
        if (success) {

            orderLogService.addLogBatch(null,
                    shippingOrderIds,
                    ShippingOrderStatusEnum.CANCELED.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getDesc(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getRemark());
            log.info("autoCloseOverdueShippingOrder.info: closed {} shipping orders", shippingOrderIds.size());
        }
        // 找出关联报价单
        List<Long> orderIds = shippingOrderRelService.listOrderIdsByShippingOrderIds(shippingOrderIds);
        if (CollUtil.isEmpty(orderIds)) {
            log.info("autoCloseOverdueShippingOrder.end: no available relation order");
            return;
        }
        // 更新为待发货
        success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .in(Order::getId, orderIds)
                .update(new Order());
        if (success) {
            // 插入自动取消日志
            orderLogService.addLogBatch(null,
                    orderIds,
                    OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getCode(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getDesc(),
                    OrderOperationEnum.AUTO_CANCEL_SHIPPING_ORDER.getRemark());
            log.info("autoCloseOverdueShippingOrder.info: rollback {} relation orders", orderIds.size());
        }
        log.info("autoCloseOverdueShippingOrder.end");
    }

    private void updateQuoteCanceled(List<Long> quotedLogIds, OrderQuoteLogSubStatusEnum subStatus, OrderOperationEnum operation) {
        if (CollUtil.isEmpty(quotedLogIds)) {
            return;
        }
        orderQuotePriceLogService.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.CANCELED.getCode())
                .set(OrderQuotePriceLog::getSubStatus, subStatus.getCode())
                .eq(AbstractBaseEntity::getDeleted, false)
                .in(OrderQuotePriceLog::getId, quotedLogIds)
                .update(new OrderQuotePriceLog());
        // 日志
        orderLogService.addLogBatch(null,
                quotedLogIds,
                OrderQuoteLogStatusEnum.CANCELED.getCode(),
                operation.getCode(),
                operation.getDesc(),
                operation.getRemark());
    }
}