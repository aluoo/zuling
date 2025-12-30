package com.anyi.common.exchange.enums;

public enum ExchangeOrderLogStatus {

    FAIL(-1, "拒绝"),
    PASS(1, "审核通过"),
    ROLLBACK(2, "重新审核"),
    ;

    Integer code;
    String name;

    ExchangeOrderLogStatus(Integer code, String name) {
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
        for (ExchangeOrderLogStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}