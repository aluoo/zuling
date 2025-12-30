package com.anyi.common.exchange.enums;

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
public enum ExchangeOrderTypeEnum {
    HUAN_JI(3, "换机"),
    ONE_KEY(4, "一键更新"),
    LV_ZHOU(5, "快手绿洲"),
    IPHONE_DOUYIN(6, "苹果抖音"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}