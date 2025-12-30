package com.anyi.sparrow.organize.employee.enums;

public enum ObjectInfoType {
    CHANNEL(1, "渠道"),
    DEPT(2, "部门"),
    EMPLOYEE(3, "员工")
    ;
    int code;
    String name;

    ObjectInfoType(int code, String name) {
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
