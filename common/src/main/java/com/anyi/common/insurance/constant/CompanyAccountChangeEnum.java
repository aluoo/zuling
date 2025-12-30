package com.anyi.common.insurance.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 员工账户变动类型枚举类
 * </p>
 *
 * @author shenbh
 * @since 2023.03.10
 */
@Getter
@AllArgsConstructor
public enum CompanyAccountChangeEnum {

    /**
     * <p>
     * 服务商充值--主类型值为2 入 ,具体类型 40010 ,用户关注类型:是<br/>
     * </p>
     * 变动账户 可用金额(加): 1 <br/>
     * 变动账户 累计收益(加):1 <br/>
     */
    recycle_recharge(2, 40010, "充值", 1, 1, 0, 0, 1, 1, 0),

    /**
     * <p>
     * 投保付款--主类型值为1出 ,具体类型 30011 ,用户关注类型:是<br/>
     * </p>
     * 变动账户 可用金额(减): 1 <br/>
     */
    insurance_pay(1, 30011, "投保付款", 1, -1, 0, 0, 0, 0, 0),
    /**
     * <p>
     * 投保退款--主类型值为2入 ,具体类型 30022 ,用户关注类型:是<br/>
     * </p>
     * 变动账户 可用金额(加): 1 <br/>
     */
    insurance_refund(2, 30022, "投保退款", 1, 1, 0, 0, 0, 0, 0),
    ;

    /**
     * 变动主类型 (0不变 1-支出 2-收入)
     */
    private Integer changeMainType;

    /**
     * 变动类型
     */
    private Integer changeDetailType;
    /**
     * 变动类型描述
     */
    private String remark;
    /**
     * 是否用户关注类型 （指只关注可用余额的变更）
     */
    private Integer isUserFocusType;
    /**
     * 变动账户可用余额(分)
     */
    private int changeAbleBalance;
    /**
     * 变动账户临时冻结金额(分)
     */
    private int changeTempFrozenBalance;
    /**
     * 变动账户被封金额(分)
     */
    private int changeFrozenBalance;
    /**
     * 变动账户钱包累计入账(分)
     */
    private int changeAccumulateIncome;

    /**
     * 变动账户 团队佣金累计入账(分)
     */
    private int changeAccAwardIncome;

    /**
     * 变动账户 累计提现(分)
     */
    private int changeAccWithdraw;


}