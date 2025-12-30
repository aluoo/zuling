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
public enum MbrOrderSubStatusEnum {
    ORDERING(1, "下单中"),
    FINISHED(2, "已完成"),
    MANUAL_CANCELED(3, "手动取消"),
    AUTO_CANCELED(4, "自动取消"),
    ;
    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}