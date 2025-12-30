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
public enum ReceivePaymentStatusEnum {
    PENDING_PAYMENT(0, "待收款"),
    PAYED(1, "已收款"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}