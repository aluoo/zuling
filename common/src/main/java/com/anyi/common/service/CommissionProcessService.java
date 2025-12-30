package com.anyi.common.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.account.service.EmployeeAccountChangeService;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderQuotePriceLog;
import com.anyi.common.product.domain.dto.LogisticsPriceDTO;
import com.anyi.common.product.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CommissionProcessService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private CommissionSettleService commissionSettleService;
    @Autowired
    private OrderService orderService;

    public void createPlatformSubsidyToStoreEmployee(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return;
        }
        List<Order> orders = orderService.lambdaQuery()
                .select(Order::getId, Order::getStoreCompanyId, Order::getStoreEmployeeId, Order::getPlatformSubsidyPrice)
                .in(Order::getId, orderIds)
                .list();
        if (CollUtil.isEmpty(orders)) {
            return;
        }
        orders.forEach(o -> {
            Integer platformSubsidyPrice = o.getPlatformSubsidyPrice();
            if (platformSubsidyPrice == null || platformSubsidyPrice == 0) {
                return;
            }
            // 增加平台补贴到做单员工账户
            employeeAccountChangeService.changeAccount(o.getStoreEmployeeId(),
                    EmployAccountChangeEnum.platform_subsidy_income,
                    Long.valueOf(platformSubsidyPrice),
                    o.getId(),
                    EmployAccountChangeEnum.platform_subsidy_income.getRemark());
        });
    }

    public void deductionRecyclerAccount(Long recyclerCompanyId, OrderQuotePriceLog confirmedQuotePriceLog, String productName) {
        Long recyclerManagerId = companyService.getCompanyManagerId(recyclerCompanyId);
        String remark = StrUtil.format("{}回收", productName);
        // 扣减原始报价
        employeeAccountChangeService.changeAccount(recyclerManagerId,
                EmployAccountChangeEnum.mobile_collect,
                Long.valueOf(confirmedQuotePriceLog.getOriginalQuotePrice()),
                confirmedQuotePriceLog.getOrderId(),
                remark);
        // 扣减平台服务费
        employeeAccountChangeService.changeAccount(recyclerManagerId,
                EmployAccountChangeEnum.recycle_plat_service,
                Long.valueOf(confirmedQuotePriceLog.getPlatformCommission()),
                confirmedQuotePriceLog.getOrderId(),
                EmployAccountChangeEnum.recycle_plat_service.getRemark());
    }

    public void deductionRecyclerAccountForLogistics(LogisticsPriceDTO req) {
        Long recyclerManagerId = companyService.getCompanyManagerId(req.getRecyclerCompanyId());
        // 扣减物流费用
        employeeAccountChangeService.changeAccount(recyclerManagerId,
                EmployAccountChangeEnum.logistics_fee,
                Long.valueOf(req.getOriginalPrice()),
                req.getShippingOrderId(),
                EmployAccountChangeEnum.logistics_fee.getRemark());
    }

    public void rollbackRecyclerAccount(Long recyclerCompanyId, OrderQuotePriceLog confirmedQuotePriceLog, String productName) {
        String remark = StrUtil.format("{}回收取消", productName);
        Long recyclerManagerId = companyService.getCompanyManagerId(recyclerCompanyId);
        // 回滚原始报价
        employeeAccountChangeService.changeAccount(recyclerManagerId,
                EmployAccountChangeEnum.mobile_collect_cancel,
                Long.valueOf(confirmedQuotePriceLog.getOriginalQuotePrice()),
                confirmedQuotePriceLog.getOrderId(),
                remark);
        // 回滚平台服务费
        employeeAccountChangeService.changeAccount(recyclerManagerId,
                EmployAccountChangeEnum.recycle_plat_service_cancel,
                Long.valueOf(confirmedQuotePriceLog.getPlatformCommission()),
                confirmedQuotePriceLog.getOrderId(),
                EmployAccountChangeEnum.recycle_plat_service_cancel.getRemark());
    }

    public void createWaitSettle(Order order, OrderQuotePriceLog confirmedQuotePriceLog) {
        // 生成待结算佣金
        // 门店分佣=门店压价
        int storeCommission = confirmedQuotePriceLog.getCommission();
        // 分佣规则绑定
        commissionSettleService.orderScaleBindSettleRule((long) storeCommission,
                order.getId(),
                CommissionBizType.PHONE_DOWN,
                CommissionPackage.PHONE_DOWN,
                order.getStoreEmployeeId());
        String remark = StrUtil.format("{}成交冻结", order.getProductName());
        // 待结算记录
        commissionSettleService.waitSettleOrder(order.getId(),
                CommissionBizType.PHONE_DOWN,
                CommissionPackage.PHONE_DOWN.getType(),
                order.getStoreEmployeeId(),
                EmployAccountChangeEnum.mobile_complete_froze,
                remark);

        // 平台服务费分佣=平台抽成金额
        Integer platformCommission = confirmedQuotePriceLog.getPlatformCommission();
        // 分佣规则绑定
        commissionSettleService.orderScaleBindSettleRule((long) platformCommission,
                order.getId(),
                CommissionBizType.PLAT_SERVICE,
                CommissionPackage.PLAT_SERVICE,
                order.getStoreEmployeeId());
        // 待结算记录
        commissionSettleService.waitSettleOrder(order.getId(),
                CommissionBizType.PLAT_SERVICE,
                CommissionPackage.PLAT_SERVICE.getType(),
                order.getStoreEmployeeId(),
                EmployAccountChangeEnum.plat_service_commission_froze,
                EmployAccountChangeEnum.plat_service_commission_froze.getRemark());
    }

    public void cancelWaitSettle(Long orderId, String productName) {
        String remark = StrUtil.format("{}成交取消", productName);
        // 回滚门店佣金待结算
        commissionSettleService.waitSettleCancel(orderId,
                CommissionBizType.PHONE_DOWN,
                CommissionPackage.PHONE_DOWN,
                EmployAccountChangeEnum.mobile_complete_cancel,
                remark);
        // 回滚平台佣金待结算
        commissionSettleService.waitSettleCancel(orderId,
                CommissionBizType.PLAT_SERVICE,
                CommissionPackage.PLAT_SERVICE,
                EmployAccountChangeEnum.plat_service_commission_froze_cancel,
                EmployAccountChangeEnum.plat_service_commission_froze_cancel.getRemark());
    }

    public void settle(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return;
        }
        Map<Long, String> productNameMap = orderService.lambdaQuery()
                .select(Order::getId, Order::getProductName)
                .in(Order::getId, orderIds)
                .list().stream().collect(Collectors.toMap(Order::getId, Order::getProductName, (k1, k2) -> k1));
        // 结算佣金
        orderIds.forEach(orderId -> {
            String name = productNameMap.get(orderId);
            String remark = StrUtil.isBlank(name)
                    ? EmployAccountChangeEnum.mobile_complete_commission.getRemark()
                    : StrUtil.format("{}成交", name);
            // 结算门店佣金
            commissionSettleService.settleOrder(orderId,
                    CommissionBizType.PHONE_DOWN,
                    CommissionPackage.PHONE_DOWN.getType(),
                    EmployAccountChangeEnum.mobile_complete_commission,
                    remark);
            // 结算平台佣金
            commissionSettleService.settleOrder(orderId,
                    CommissionBizType.PLAT_SERVICE,
                    CommissionPackage.PLAT_SERVICE.getType(),
                    EmployAccountChangeEnum.plat_service_commission,
                    EmployAccountChangeEnum.plat_service_commission.getRemark());
        });
    }
}