package com.anyi.common.commission.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 佣金结算来源类型枚举类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-08
 */
@Getter
@AllArgsConstructor
public enum CommissionBizType {

    /**
     * 1-平台服务费
     */
    PLAT_SERVICE(1L, "平台服务费"),
    /**
     * 2-APP拉新
     */
    APP_NEW(2L, "APP拉新"),
    /**
     * 3-手机压价
     */
    PHONE_DOWN(3L, "手机压价"),
    /**
     * 4-享转数保
     */
    INSURANCE_SERVICE(4L, "享转数保"),
    /**
     * 5-号卡拉新
     */
    HK_SERVICE(5L, "号卡拉新"),
    ;

    private Long type;

    private String typeName;

}
