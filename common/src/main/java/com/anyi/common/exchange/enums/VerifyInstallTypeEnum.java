package com.anyi.common.exchange.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum VerifyInstallTypeEnum {
    LV_ZHOU("lvzhou", "快手绿洲"),
    IPHONE_DOUYIN("pgdouyin", "苹果抖音"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}