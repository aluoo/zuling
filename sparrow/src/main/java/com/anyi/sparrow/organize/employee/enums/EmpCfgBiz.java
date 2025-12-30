package com.anyi.sparrow.organize.employee.enums;

public enum EmpCfgBiz {
    QL_ICBC(1, "齐鲁工行"),
    QL_QL(2, "齐鲁"),
    JS_BCM(3, "江苏交行"),
    ;
    int code;
    String name;

    EmpCfgBiz(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
