package com.anyi.common.notice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/15
 */
@AllArgsConstructor
public enum MsgReadStatusEnum {
    UNREAD(0, "未读"),
    READ(1, "已读");

    @Getter
    private final Integer code;
    @Getter
    private final String name;
}