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
public enum MbrPreOrderStatusEnum {

    CANCELED(-1, "已关闭"),
    ORDERING(0, "待发送"),
    DEALING(1, "待处理"),
    FINISHED(2, "已处理"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}