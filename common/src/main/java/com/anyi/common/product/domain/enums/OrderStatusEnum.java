package com.anyi.common.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum OrderStatusEnum {
    // 退款->已退款
    REFUNDED(-2, "已退款"),
    // 手动/自动取消->已作废/已取消
    CANCELED(-1, "已作废"),
    // 已创建->待确认/待报价
    UNCHECKED(0, "待确认"),
    // 已确认->待收款
    PENDING_PAYMENT(1, "待收款"),
    // 已收款&绑码&核验->待发货
    PENDING_SHIPMENT(2, "待发货"),
    // 发货 -> 待收货
    PENDING_RECEIPT(3, "待收货"),
    // 确认收货->已完成
    FINISHED(4, "已完成"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}