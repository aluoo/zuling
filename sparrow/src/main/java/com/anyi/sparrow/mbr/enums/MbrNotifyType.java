package com.anyi.sparrow.mbr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;



@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MbrNotifyType {
    PassOrder("sales.saleOrder.createOrder", "审核通过"),
    CreteOrder("sales.saleOrder.initOrder", "创建订单"),
    ChangeOrder("sales.saleOrder.changeOrder", "更新订单"),
    ;

    String code;
    String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
