package com.anyi.sparrow.common.enums;

public enum  UserType {
    EMPLOYEE(1, "员工"),
    USER(2, "用户"),
    TEMPORARY_EMPLOYEE(3, "临时员工"),
    INSURANCE_EMPLOYEE(4, "数保用户"),
    ;

    int code;
    String name;

    UserType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}