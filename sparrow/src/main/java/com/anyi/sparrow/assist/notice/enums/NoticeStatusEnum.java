package com.anyi.sparrow.assist.notice.enums;

public enum NoticeStatusEnum {
    ENABLE((byte)1, "启用"),
    STOP_USED((byte)2, "停用");


    private byte code;

    private String name;

    NoticeStatusEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
