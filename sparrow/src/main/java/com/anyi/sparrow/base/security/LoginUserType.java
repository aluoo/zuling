package com.anyi.sparrow.base.security;

public enum LoginUserType {
    EMP(1, "员工"),
    USER(2, "小程序");

    LoginUserType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    Integer code;
    String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
