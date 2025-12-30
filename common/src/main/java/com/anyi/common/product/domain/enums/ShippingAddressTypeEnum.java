package com.anyi.common.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description 寄件类型枚举类
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum ShippingAddressTypeEnum {
    // 寄出方
    SEND(1, "寄出方"),
    // 收货方
    RECEIVE(2, "收货方"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}