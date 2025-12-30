package com.anyi.common.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description 支付方式枚举类
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum PaymentTypeEnum {
    // 支付方式 - 支付宝
    ALIPAY(1, "支付宝"),
    // 支付方式 - 微信
    WECHAT(2, "微信"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}