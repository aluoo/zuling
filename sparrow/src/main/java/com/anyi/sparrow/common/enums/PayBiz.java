package com.anyi.sparrow.common.enums;

public enum PayBiz {
    USER_ETC_ORDER("1", "微信小程序开卡");

    String code;
    String name;

    PayBiz(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
