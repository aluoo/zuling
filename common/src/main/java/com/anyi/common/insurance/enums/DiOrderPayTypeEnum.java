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
public enum DiOrderPayTypeEnum {
    WECHAT(1, "微信"),
    ACCOUNT(2, "账户余额"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}