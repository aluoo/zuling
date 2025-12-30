package com.anyi.sparrow.applet.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 用户信用分变动类型枚举类
 * </p>
 *
 * @author shenbh
 * @since  2023.05.16
 */
@Getter
@AllArgsConstructor
public enum WmCreditScoreChangeEnum {


    /**
     * <p>
     * 业务办理 主类型值为2 入 ,具体类型 2001 <br/>
     * </p>
     *
     * 变动账户信用分 (增):1 <br/>
     * 变动变动账户暂存信用分(不变): 0 <br/>
     */
    PAY_PACKAGE(2, 2001, "业务办理", 1, 0),
    /**
     * <p>
     * 解黑 主类型值为2 入 ,具体类型 2002 <br/>
     * </p>
     *
     * 变动账户信用分 (增):1 <br/>
     * 变动变动账户暂存信用分(减): -1 <br/>
     */
    REMOVE_BLACK(2, 2002, "解黑", 1, -1),
    /**
     * <p>
     * 通行还款 主类型值为2 入 ,具体类型 2003 <br/>
     * </p>
     *
     * 变动账户信用分 (增):1 <br/>
     * 变动变动账户暂存信用分(不变): 0 <br/>
     */
    REPAYMENT(2, 2003, "通行还款", 1, 0),
    /**
     * <p>
     * 持续多笔通行还款 主类型值为2 入 ,具体类型 2004 <br/>
     * </p>
     *
     * 变动账户信用分 (增):1 <br/>
     * 变动变动账户暂存信用分(不变): 0 <br/>
     */
    CONTINUE_REPAYMENT(2, 2004, "持续多笔通行还款", 1, 0),
    /**
     * <p>
     * 通行欠费 主类型值为1 支出 ,具体类型 1001 <br/>
     * </p>
     *
     * 变动账户信用分 (减):-1 <br/>
     * 变动变动账户暂存信用分(不变): 0 <br/>
     */
    OWE_FEE(1, 1001, "通行欠费", -1, 0),
    /**
     * <p>
     * 拉黑 主类型值为1 支出 ,具体类型 1002 <br/>
     * </p>
     *
     * 变动账户信用分 (减):-1 <br/>
     * 变动变动账户暂存信用分(增): 1 <br/>
     */
    MOVE_BLACK(1, 1002, "拉黑", -1, 1),


    ;

    /**
     * 变动主类型 (0不变 1-支出 2-收入)
     */
    private Integer changeState;

    /**
     * 具体变动类型 (2001-业务办理  2002-解黑  2003. 通行还款  2004.持续多笔通行还款  1001. 通行欠费  1002. 拉黑)
     */
    private Integer changeType;

    /**
     * 变动类型描述
     */
    private String remark;
    /**
     * 变动账户信用分(分)
     */
    private int changeScore;
    /**
     * 变动账户暂存信用分(分)
     */
    private int changeTempScore;






}
