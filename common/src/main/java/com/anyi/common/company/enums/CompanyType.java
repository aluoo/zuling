package com.anyi.common.company.enums;

public enum CompanyType {
    COMPANY(1, "公司"),
    STORE(2, "门店"),
    CHAIN(3, "连锁"),
    RECYCLE(4, "回收商");
    int code;
    String name;

    CompanyType(int code, String name) {
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
