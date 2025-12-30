package com.anyi.sparrow.assist.projects.enums;

public enum DeviceType {


    ANDROID(1, "android"), IOS(2, "IOS");

    private int code;

    private String msg;

    DeviceType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
