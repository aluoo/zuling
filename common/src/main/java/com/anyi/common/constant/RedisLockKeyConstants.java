package com.anyi.common.constant;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/7/18
 */
public class RedisLockKeyConstants {
    /**
     * 支付宝转账收款分布式锁key
     */
    public static final String ALIPAY_RECEIVE_PAYMENT_TRANSFER_LOCK_KEY = "alipay_receive_payment_transfer_lock_orderId-{}";
    public static final String WX_PAY_REFUND_PAYMENT_PAY_LOCK_KEY = "wx_pay_refund_payment_pay_lock_orderId-{}";
    public static final String WX_PAY_REFUND_PAYMENT_PAY_SUCCESS_LOCK_KEY = "wx_pay_refund_payment_pay_success_lock_otn-{}";
    public static final String MB_ORDER_RECYCLER_QUOTE_LOCK_KEY = "mb_order_recycler_quote_lock_quoteLogId-{}";
    public static final String MB_ORDER_RECYCLER_CONFIRM_RECEIPT_LOCK_KEY = "mb_order_recycler_confirm_receipt_lock_shippingOrderId-{}";
    public static final String WX_PAY_DI_PAYMENT_PAY_LOCK_KEY = "wx_pay_di_payment_pay_lock_orderId-{}";
    public static final String WX_PAY_DI_PAYMENT_PAY_SUCCESS_LOCK_KEY = "wx_pay_di_payment_pay_success_lock_otn-{}";
    public static final String MBR_SHOP_CODE_LOCK_KEY = "mbr_shop_code_lock-{}";
}