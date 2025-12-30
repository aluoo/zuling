package com.anyi.sparrow.organize.companyCfg.enums;

public enum SkipLicEnums {
    NOT_SKIP(0, "不跳过"),
    SKIP(1, "跳过");

    int code;
    String name;

    SkipLicEnums(int code, String name) {
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
