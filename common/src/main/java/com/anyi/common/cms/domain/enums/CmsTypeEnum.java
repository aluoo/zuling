package com.anyi.common.cms.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@AllArgsConstructor
public enum CmsTypeEnum {
    LIBRARY(1, "LIBRARY", "知识库"),
    FAQ(2, "FAQ", "常见问题"),
    ;

    @Getter
    private final Integer type;
    @Getter
    private final String bizType;
    @Getter
    private final String desc;
}