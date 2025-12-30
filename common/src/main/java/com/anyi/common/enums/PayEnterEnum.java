package com.anyi.common.enums;

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
public enum PayEnterEnum {
    // 支付申请退款金额入口
    CUSTOMER_REFUND_PAYMENT("/mobile/customer/refund/payment/pay", 1, "购机款退还支付"),
    CUSTOMER_REFUND_PAYMENT_NOTIFY("/mobile/customer/refund/payment/notify", 1, "购机款退还支付回调"),
    DI_ORDER_PAYMENT("/insurance/order/pay/wechat", 2, "数保订单支付"),
    DI_ORDER_PAYMENT_NOTIFY("/insurance/order/payment/notify", 2, "数保订单支付回调"),
    ;

    @Getter
    private final String url;
    @Getter
    private final Integer bizType;
    @Getter
    private final String desc;
}