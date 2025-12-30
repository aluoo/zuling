package com.anyi.sparrow.organize.employee.enums;

public enum DeptType {
    MANGER(1, "管理部门"),
    COMMON(2, "普通部门")
    ;
    int code;
    String name;

    DeptType(int code, String name) {
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
