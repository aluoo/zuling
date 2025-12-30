package com.anyi.sparrow.assist.verify.enums;

public enum Status {
    NOT_USED((byte)0, "未使用"),
    USED((byte)1, "已使用")

    ;
    private Byte code;

    private String msg;

    Status(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Byte getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
