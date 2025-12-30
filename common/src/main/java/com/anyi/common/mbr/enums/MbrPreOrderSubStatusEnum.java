package com.anyi.common.mbr.enums;

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
public enum MbrPreOrderSubStatusEnum {
    ORDERING(0, "待发送"),
    DEALING(1, "待处理"),
    FINISHED(2, "已处理"),
    PASS(4, "审核通过"),
    FAIL(5, "审核拒绝"),
    MANUAL_CANCELED(6, "手动取消"),
    AUTO_CANCELED(7, "自动取消"),
    ;
    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}