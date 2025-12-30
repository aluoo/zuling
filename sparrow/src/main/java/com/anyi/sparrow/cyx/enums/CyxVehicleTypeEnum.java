package com.anyi.sparrow.cyx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@AllArgsConstructor
public enum CyxVehicleTypeEnum {
    BUS_FIRST("1", "一型客车"),
    BUS_SECOND("2", "二型客车"),
    BUS_THIRD("3", "三型客车"),
    BUS_FOURTH("4", "四型客车"),
    TRUCK_FIRST("11", "一型货车"),
    TRUCK_SECOND("12", "二型货车"),
    TRUCK_THIRD("13", "三型货车"),
    TRUCK_FOURTH("14", "四型货车"),
    TRUCK_FIFTH("15", "五型货车"),
    TRUCK_SIXTH("16", "六型货车"),
    //special operation vehicle
    SOV_FIRST("21", "一型专项作业车"),
    SOV_SECOND("22", "二型专项作业车"),
    SOV_THIRD("23", "三型专项作业车"),
    SOV_FOURTH("24", "四型专项作业车"),
    SOV_FIFTH("25", "五型专项作业车"),
    SOV_SIXTH("26", "六型专项作业车"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}