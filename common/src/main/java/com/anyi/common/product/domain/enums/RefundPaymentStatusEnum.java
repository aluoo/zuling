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
public enum RefundPaymentStatusEnum {
    PENDING_PAYMENT(0, "待支付"),
    PAYED(1, "已支付"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}