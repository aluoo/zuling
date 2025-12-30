package com.anyi.sparrow.withdraw.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卡异常类型
 *
 * @author shenbh
 * @since 2023/3/23
 */
@Getter
@AllArgsConstructor
public enum WithdrawCardIllegalTypeEnum {

    /**
     * INFO_ERROR 账户信息不一致，请重新绑定
     */
    INFO_ERROR("账户信息不一致，请重新绑定"),

    /**
     * STATUS_ERROR 账户状态异常，请重新绑定
     */
    STATUS_ERROR("账户状态异常，请重新绑定"),


    ;

    private String message;
}
