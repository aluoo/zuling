package com.anyi.common.commission.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 系统结算状态枚举类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-08
 */
@Getter
@AllArgsConstructor
public enum SettleStatus {

    /**
     * 0-不结算
     */
    NO_SETTLE(0),

    /**
     * 1-待结算
     */
    WAIT_TO_SETTLE(1),

    /**
     * 2-已结算
     */
    HAVING_SETTLE(2),
    ;

    private int status;

}
