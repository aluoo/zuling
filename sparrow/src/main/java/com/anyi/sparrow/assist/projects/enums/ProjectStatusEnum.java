package com.anyi.sparrow.assist.projects.enums;

public enum ProjectStatusEnum {

    READY(1, "未开启"), NOW(2, "当前版本"), EXPIRED(3, "已过期");

    private int code;

    private String msg;

    ProjectStatusEnum(int code, String msg) {
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
