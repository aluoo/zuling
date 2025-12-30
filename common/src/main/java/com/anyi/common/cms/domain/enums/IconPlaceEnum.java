package com.anyi.common.cms.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@AllArgsConstructor
public enum IconPlaceEnum {
    INDEX(1, "首页"),
    EXCHANGE(2, "换机首页"),
    ;

    @Getter
    private final Integer type;
    @Getter
    private final String desc;
}