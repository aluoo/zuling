package com.anyi.common.company.enums;


public enum CompanyStatus {
    TO_AUDIT((byte)1, "待审核"),
    NORMAL((byte)2, "正常"),
    FAILED((byte)3, "审核失败"),
    CANCEL((byte)4, "注销"),
    FREEZE((byte)5, "冻结"),
    ;
    Byte code;
    String name;

    CompanyStatus(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public Byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Byte code) {
        for (CompanyStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
