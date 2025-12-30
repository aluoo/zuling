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
public enum ShippingTypeEnum {
    // 线上
    ONLINE(1, "线上"),
    // 线下
    OFFLINE(2, "线下"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}