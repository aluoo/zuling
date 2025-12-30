package com.anyi.common.insurance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenjian
 * @Description
 * @Date 2024/9/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum DiReadImeiEnum {
    READ(true, "可调取串号"),
    UN_READ(false, "不可调取串号"),
    ;

    @Getter
    private final Boolean code;
    @Getter
    private final String desc;
}