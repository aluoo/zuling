package com.anyi.common.withdraw.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 提现申请状态枚举
 *
 * @author shenbh
 * @since 2023/3/30
 */
@Getter
@AllArgsConstructor
public enum WithdrawApplyStatusEnum {

    /**
     * -1 失败
     */
    fail(-1),

    /**
     * 0 待审核
     */
    wait_audit(0),

    /**
     * 1 审核通过，待打款
     */
    wait_pay(1),

    /**
     * 2 打款成功
     */
    pay_success(2),

    /**
     * 3 打款中
     */
    paying(3),
    ;

    private int type;

}