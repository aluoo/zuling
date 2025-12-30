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
public enum DiOrderSubStatusEnum {
    PENDING_PAYMENT(0, "待支付"),
    PENDING_AUDIT_ORDER(1, "资料待审核"),
    PENDING_AUDIT_REFUND(2, "退款待审核"),
    PENDING_UPLOAD_INSURANCE(3, "待上传"),
    PENDING_EFFECTIVE(4, "待出保"),
    EFFECTIVE_FINISHED(5, "已出保"),
    AUDIT_ORDER_FAILED(6, "资料审核失败"),
    AUDIT_REFUND_FAILED(7, "退款审核失败"),
    MANUAL_CANCELED(8, "手动取消"),
    AUTO_CANCELED(9, "自动取消"),
    REFUNDED(10, "已退款"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}