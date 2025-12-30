package com.anyi.sparrow.applet.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CreditTypeEnum {
    //3-订单已退费 4-充值通行积分 5-注销费积分抵扣  6-无责注销积分清空

    REGIST_ETC(1, "业务办理充值"),
    PAYMENT(2, "通行费垫付"),
    REFUND(3, "订单已退费"),
    RECHARGE(4, "充值通行积分"),
    LOGOUT(5, "注销费积分抵扣"),
    FREE_LOGOUT(6, "无责注销积分清空");

    private final Integer code;
    private final String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
