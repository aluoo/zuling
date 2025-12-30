package com.anyi.sparrow.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/15
 */
@AllArgsConstructor
public enum SysContentStatusEnum {
    WAIT(1, "待发布"),
    PUBLISH(2, "已发布"),
    STOP(3, "已停用");

    @Getter
    private final Integer code;
    @Getter
    private final String name;
}