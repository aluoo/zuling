package com.anyi.common.insurance.enums;

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
public enum DiOrderPayStatusEnum {
    PENDING_PAYMENT(0, "待支付"),
    PAYED(1, "已支付"),
    PENDING_REFUND(2, "退款中"),
    REFUNDED(3, "已退款"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}