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
public enum PartnerChanelEnum {
    JXZSHUZHIYUN("JXZSHUZHIYUN", "数智云"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}