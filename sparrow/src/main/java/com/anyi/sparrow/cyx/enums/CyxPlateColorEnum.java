package com.anyi.sparrow.cyx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@AllArgsConstructor
public enum CyxPlateColorEnum {
    BLUE("0", "蓝色"),
    YELLOW("1", "黄色"),
    BLACK("2", "黑色"),
    WHITE("3", "白色"),
    GRADIENT_GREEN("4", "渐变绿色"),
    YELLOW_GREEN("5", "黄绿双拼色"),
    BLUE_WHITE("6", "蓝白渐变色"),
    TEMPORARY_LICENCE("7", "临时牌照"),
    UNDETERMINED("9", "未确定"),
    GREEN("11", "绿色"),
    RED("12", "红色"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}