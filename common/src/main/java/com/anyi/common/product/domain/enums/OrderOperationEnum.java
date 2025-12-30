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
public enum OrderOperationEnum {
    CREATE(1, "创建订单", "创建订单"),
    CONFIRM_QUOTE(2, "确认报价", "确认报价"),
    APPLY_PAY_INFO(3, "提交收款信息", "提交收款信息"),
    PAY(4, "收款", "收款"),
    APPLY_REFUND_INFO(5, "提交退款信息", "提交退款信息"),
    REFUND(6, "退款", "退款"),
    BOUND(7, "绑码", "绑码"),
    VERIFIED(8, "核验", "核验"),
    CONFIRM_SHIPMENT(9, "发货", "发货"),
    CONFIRM_RECEIPT(10, "确认收货", "确认收货"),
    MANUAL_CLOSE(11, "手动取消订单", "取消订单"),
    AUTO_CLOSE(12, "自动取消订单", "超时自动取消订单"),
    AUTO_CLOSE_QUOTE(13, "超时自动关闭报价", "超时自动关闭报价"),
    AUTO_CLOSE_RECEIVE_PAYMENT(14, "收款码超时作废", "收款码超时作废"),
    AUTO_CLOSE_ORDER_WHEN_NO_QUOTE(15, "自动取消订单", "超时无报价自动取消订单"),

    INIT_QUOTE(21, "创建报价记录", "创建报价记录"),
    LOCK_QUOTE(22, "抢单", "抢单"),
    QUOTE(23, "报价", "报价"),
    CANCEL_LOCK_QUOTE(24, "取消报价", "取消报价，原因：{}，备注：{}"),
    QUOTE_CONFIRMED(25, "已确认交易", "已确认交易"),
    QUOTE_CANCELED_NOT_CONFIRMED(26, "已作废-未入选", "已作废-未入选"),
    QUOTE_CANCELED_OVERDUE(27, "已作废-超时未报价", "已作废-超时未报价"),
    QUOTE_CANCELED_CONFIRM_OVERDUE(28, "已作废-超时未确认交易", "已作废-超时未确认交易"),
    QUOTE_CANCELED_CANCEL_TRADE(29, "已作废-交易取消", "已作废-交易取消"),
    QUOTE_CANCELED_REFUND_TRADE(30, "已作废-交易退款", "已作废-交易退款"),
    REJECT_QUOTE(31, "拒绝报价", ""),

    CREATE_SHIPPING_ORDER(51, "创建发货订单", "创建发货订单"),
    LOGISTICS_ORDER_OFFLINE(52, "物流下单-线下寄出", "物流下单-线下寄出"),
    LOGISTICS_ORDER_ONLINE(53, "物流下单-线上寄出", "物流下单-线上寄出"),
    CANCEL_SHIPPING_ORDER(54, "取消发货", "取消发货"),
    AUTO_CANCEL_SHIPPING_ORDER(55, "超时自动取消发货", "超时自动取消发货"),

    // 数保订单操作
    DI_ORDER_CREATE(80, "创建订单", "创建订单"),
    DI_ORDER_PAY(81, "支付成功", "支付成功"),
    DI_ORDER_REFUND_APPLY(82, "申请退款", "申请退款"),
    DI_ORDER_REFUND_AUDIT_FAILED(83, "退款审核失败", "退款审核失败"),
    DI_ORDER_REFUND_AUDIT_PASSED(84, "退款审核通过", "退款审核通过"),
    DI_ORDER_AUDIT_FAILED(85, "资料审核失败", "资料审核失败"),
    DI_ORDER_EDIT_INFO_CUSTOMER(86, "客户修改资料", "客户修改资料"),
    DI_ORDER_AUDIT_PASSED(87, "资料审核通过", "资料审核通过"),
    DI_ORDER_UPLOAD_FAILED(88, "保单上传失败", "保单上传失败"),
    DI_ORDER_UPLOAD_PASSED(89, "保单上传成功", "保单上传成功"),
    DI_ORDER_EDIT_INFO_ADMIN(90, "后台修改资料", "后台修改资料"),
    DI_ORDER_EFFECTIVE_FINISHED(91, "保单出保成功", "保单出保成功"),
    DI_ORDER_CANCEL_MANUAL(92, "手动取消订单", "取消订单"),
    DI_ORDER_CANCEL_AUTO(93, "自动取消订单", "超时自动取消订单"),
    DI_ORDER_REFUND(94, "已退款", "退款成功"),

    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
    @Getter
    private final String remark;
}