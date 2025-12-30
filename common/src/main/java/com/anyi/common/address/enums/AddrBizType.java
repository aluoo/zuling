package com.anyi.common.address.enums;

public enum AddrBizType {
    MOV_ORDER("mov-order", "库存调拨");

    String code;
    String name;

    AddrBizType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
