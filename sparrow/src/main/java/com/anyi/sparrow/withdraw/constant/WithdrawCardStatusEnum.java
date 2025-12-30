package com.anyi.sparrow.withdraw.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 提现卡状态类型
 *
 * @author shenbh
 * @since 2023/3/30
 */
@Getter
@AllArgsConstructor
public enum WithdrawCardStatusEnum {

    /**
     * 正常 1
     */
    normal(1),

    /**
     * 异常 2
     */
    abnormal(2),

    /**
     * 待审核 0
     */
    pending_audit(0),

    ;

    private int type;
}
