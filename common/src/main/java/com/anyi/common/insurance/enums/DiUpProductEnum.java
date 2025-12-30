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
public enum DiUpProductEnum {
    // 提交待审核
    SAME(1, "同款同型"),
    UP(2, "升级迭代"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}