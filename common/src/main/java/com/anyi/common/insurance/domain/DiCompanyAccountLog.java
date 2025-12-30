package com.anyi.common.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数保门店账户变动明细表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_company_account_log")
@ApiModel(value = "DiCompanyAccountLog对象", description = "数保门店账户变动明细表")
public class DiCompanyAccountLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("说明")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("账户id")
    private Long companyAccountId;

    @ApiModelProperty("门店ID")
    private Long companyId;

    @ApiModelProperty("组织层级编码")
    private String ancestors;

    @ApiModelProperty("主变动类型(收入、支出、不变)")
    private Integer changeMainType;

    @ApiModelProperty("详细变动类型")
    private Integer changeDetailType;

    @ApiModelProperty("变动金额")
    private Long changeBalance;

    @ApiModelProperty("钱包可用余额(分)_变动前")
    private Long ableBalanceBefore;

    @ApiModelProperty("钱包可用余额(分)_变动金额")
    private Long ableBalanceChange;

    @ApiModelProperty("钱包可用余额(分)_变动后")
    private Long ableBalanceAfter;

    @ApiModelProperty("钱包冻结金额(分)_变动前")
    private Long tempFrozenBalanceBefore;

    @ApiModelProperty("钱包冻结金额(分)_变动金额")
    private Long tempFrozenBalanceChange;

    @ApiModelProperty("钱包冻结金额(分)_变动后")
    private Long tempFrozenBalanceAfter;

    @ApiModelProperty("钱包永久冻结金额(分)_变动前")
    private Long frozenBalanceBefore;

    @ApiModelProperty("钱包永久冻结金额(分)_变动金额")
    private Long frozenBalanceChange;

    @ApiModelProperty("钱包永久冻结金额(分)_变动后")
    private Long frozenBalanceAfter;

    @ApiModelProperty("钱包累计入账(分)_变动前")
    private Long accumulateIncomeBefore;

    @ApiModelProperty("钱包累计入账(分)")
    private Long accumulateIncomeChange;

    @ApiModelProperty("钱包累计入账(分)")
    private Long accumulateIncomeAfter;

    @ApiModelProperty("奖金累计收入-变动前")
    private Long accAwardIncomeBefore;

    @ApiModelProperty("奖金累计收入-变动金额")
    private Long accAwardIncomeChange;

    @ApiModelProperty("奖金累计收入-变动后")
    private Long accAwardIncomeAfter;

    @ApiModelProperty("累计提现金额-变动前")
    private Long accWithdrawBefore;

    @ApiModelProperty("累计提现金额-变动金额")
    private Long accWithdrawChange;

    @ApiModelProperty("累计提现金额-变动后")
    private Long accWithdrawAfter;

    @ApiModelProperty("扩展id")
    private Long correlationId;

    @ApiModelProperty("变动说明")
    private String remark;

    @ApiModelProperty("是否用户可见(0-否,1-是)")
    private Integer userFocus;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
