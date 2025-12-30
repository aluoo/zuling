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
public enum MbrQuoteLogStatusEnum {

    CANCELED(-1, "已拒绝"),
    ORDERING(0, "审核中"),
    FINISHED(1, "审核通过"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}