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
public enum DiOrderStatusEnum {
    REFUNDING(-2, "退款中"),
    CANCELED(-1, "已关闭"),
    PENDING_PAYMENT(0, "待支付"),
    PENDING_AUDIT(1, "待审核"),
    FINISHED(2, "已完成"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}