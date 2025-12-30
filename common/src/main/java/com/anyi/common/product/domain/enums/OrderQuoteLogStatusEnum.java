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
public enum OrderQuoteLogStatusEnum {
    // 已作废
    CANCELED(-1, "已作废"),
    // 待报价
    PENDING_QUOTE(0, "待报价"),
    // 报价中
    QUOTING(1, "报价中"),
    // 已报价
    QUOTED(2, "已报价"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}