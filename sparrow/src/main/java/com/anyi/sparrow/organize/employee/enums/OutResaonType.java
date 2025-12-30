package com.anyi.sparrow.organize.employee.enums;

public enum OutResaonType {
    KICK((byte)1, "被挤下线"),
    LOGOUT((byte)2, "主动退出");

    byte code;
    String name;

    OutResaonType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
