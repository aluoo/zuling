package com.anyi.common.exchange.enums;

public enum ExchangeSettleStatus {
    TO_AUDIT(0, "未结"),
    PASS(1, "结算");

    Integer code;
    String name;

    ExchangeSettleStatus(Integer code, String name) {
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
        for (ExchangeSettleStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
