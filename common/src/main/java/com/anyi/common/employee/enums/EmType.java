package com.anyi.common.employee.enums;

public enum EmType {
    MANGER_MANGER(1, "管理部门管理员"),
    MANGER_EM(2, "管理部门员工"),
    CM_MANGER(3, "普通部门管理员"),
    CM_EM(4, "普通部门员工"),
    TEMP_EM(-1, "临时员工"),
    ;
    int code;
    String name;

    EmType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}