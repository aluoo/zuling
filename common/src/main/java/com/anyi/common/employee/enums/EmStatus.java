package com.anyi.common.employee.enums;

import com.anyi.common.company.enums.CompanyStatus;

public enum EmStatus {
    NORMAL((byte) 1, "正常"),
    CANCEL((byte) 2, "注销"),
    FREEZE((byte) 3, "冻结"),
    ;

    Byte code;
    String name;

    EmStatus(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public Byte getCode() {
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

    public static String getNameByCode(Byte code) {
        for (EmStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
