package com.anyi.common.insurance.enums;

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
public enum DiFixTypeEnum {
    SELF(1, "自行维修"),
    SEND(2, "寄出维修"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}