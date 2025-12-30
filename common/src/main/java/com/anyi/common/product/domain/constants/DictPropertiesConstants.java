package com.anyi.common.product.domain.constants;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
public class DictPropertiesConstants {
    public static final String DICT_NAME_CACHE_PREFIX = "dict:name:";
    /**
     * 报价订单过期时间 (单位: 分钟)
     */
    public static final String PRODUCT_ORDER_EXPIRED_MINUTES = "product_order_expired_minutes";
    /**
     * 报价订单报价功能过期时间 (单位: 分钟)
     */
    public static final String PRODUCT_ORDER_QUOTE_EXPIRED_MINUTES = "product_order_quote_expired_minutes";
    /**
     * 发货订单过期时间 (单位: 分钟)
     */
    public static final String SHIPPING_ORDER_EXPIRED_MINUTES = "shipping_order_expired_minutes";
    /**
     * 平台设置的报价预警金额，超过该金额提示预警
     */
    public static final String QUOTE_WARNING_THRESHOLD_PRICE = "quote_warning_threshold_price";
    /**
     * 当天发货单笔金额阈值，大于等于此值时截止至当天发货，否则截止至本周末发货，默认为200元，单位分
     */
    public static final String SHIPPING_OVER_DUE_TIME_THRESHOLD_PRICE = "shipping_over_due_time_threshold_price";
    /**
     * 线上物流开关 0关1开
     */
    public static final String SHIPPING_ONLINE_SWITCH = "shipping_online_switch";
    /**
     * 报价单退款支付描述内容
     */
    public static final String REFUND_PAYMENT_DESCRIPTION = "mb_order_refund_payment_description";
    public static final String DI_INSURANCE_ORDER_PAYMENT_DESCRIPTION = "di_insurance_order_payment_description";
    /**
     * 订单备注选项
     */
    public static final String ORDER_REMARK_DICT_KEY = "mobile_order_remark_options";
    public static final String REJECT_QUOTE_REASON_DICT_KEY = "mobile_reject_quote_reason_options";
    /**
     * 京东物流月结折扣费率 0.00~1.00
     */
    public static final String JDL_LOGISTICS_DISCOUNT_RATE = "jdl_logistics_discount_rate";
    /**
     * 支付宝收款码失效时间 (单位: 分钟) 设置0时不过期
     */
    public static final String ALIPAY_RECEIVE_PAYMENT_EXPIRED_MINUTES = "alipay_receive_payment_expired_minutes";
    /**
     * 数保订单过期时间 (单位: 分钟)
     */
    public static final String INSURANCE_ORDER_EXPIRED_MINUTES = "insurance_order_expired_minutes";
    /**
     * 数保订单自动上传时间 (单位: 天)
     */
    public static final String INSURANCE_ORDER_AUTO_UPLOAD_DAY = "insurance_order_auto_upload_day";
    /**
     * 数保订单退款原因
     */
    public static final String INSURANCE_ORDER_REFUND_REASON_DICT_KEY = "insurance_order_refund_reason";
    /**
     * 游客注册功能开关
     */
    public static final String TEMPORARY_FUNC_ABLE = "temporary_func_able";

    public static final String RTA_FUNC_ABLE = "rta_func_able";

    /**
     * 数保报修坏机器补差
     */
    public static final String BAD_FIX_INSURANCE_PRICE = "bad_fix_insurance_price";

    /**
     * 数保报修好机器补差
     */
    public static final String GOOD_FIX_INSURANCE_PRICE = "good_fix_insurance_price";
}