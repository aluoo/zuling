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
public enum ShippingOrderStatusEnum {
    CANCELED(-1, "已取消"),
    PENDING_SHIPMENT(0, "待下单"),
    PENDING_RECEIPT(1, "待收货"),
    FINISHED(2, "已收货"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}