package com.anyi.sparrow.organize.employee.enums;

public enum DeptStatus {
    NORMAL(1, "正常"),
    CANCEL(2, "注销"),
    FREEZE(3, "冻结"),
    ;
    int code;
    String name;

    DeptStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
