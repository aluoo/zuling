package com.anyi.sparrow.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/15
 */
@AllArgsConstructor
public enum SysContentTypeEnum {
    IMPORTANCE(1, "重要"),
    NORMAL(2, "普通");

    @Getter
    private final Integer code;
    @Getter
    private final String name;
}