package com.anyi.common.exchange.enums;

public enum ExchangeStatus {
    SYS_Fail(-2, "系统审核失败"),
    FAIL(-1, "审核失败"),
    TO_AUDIT(0, "待审核"),
    SYS_PASS(1, "系统审核通过"),
    PASS(2, "审核通过");

    Integer code;
    String name;

    ExchangeStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code) {
        for (ExchangeStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
