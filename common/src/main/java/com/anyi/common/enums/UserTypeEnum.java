package com.anyi.common.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {
    ADMIN_USER(1),
    MINIAPP_USER(2),
    H5_TEMP_USER(4),
    EXCHANGE_PHONE_TOOL_USER(5);
    ;
    private final Integer code;

    UserTypeEnum(Integer code) {
        this.code = code;
    }
}