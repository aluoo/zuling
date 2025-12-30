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
public enum OrderQuoteLogSubStatusEnum {
    // 待报价
    PENDING_QUOTE(0, "待报价"),
    // 报价中
    QUOTING(1, "报价中"),
    // 已确认交易
    CONFIRMED(2, "已确认交易"),
    // 作废-未入选（门店选择其它报价方成交）
    NOT_CONFIRM(3, "未入选"),
    // 作废-超时未报价
    QUOTE_OVERDUE(4, "超时未报价"),
    // 作废-门店超时未确认交易
    CONFIRM_OVERDUE(5, "超时未确认交易"),
    // 作废-门店确认交易后取消交易
    CANCEL_TRADE(6, "取消交易"),
    // 作废-门店确认交易后退款
    REFUND_TRADE(7, "交易退款"),
    // 已报价-回收商拒绝报价
    REJECT_QUOTE(8, "拒绝报价"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}