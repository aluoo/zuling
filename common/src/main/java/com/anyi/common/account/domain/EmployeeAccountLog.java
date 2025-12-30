package com.anyi.common.account.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 个人账户变动明细表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Data
@TableName("employee_account_log")
@ApiModel(value = "EmployeeAccountLog对象", description = "个人账户变动明细表")
public class EmployeeAccountLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("说明")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeAccountId;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("组织层级编码")
    private String ancestors;

    @ApiModelProperty("主变动类型(收入、支出、不变)")
    private Integer changeMainType;

    @ApiModelProperty("详细变动类型个人发行佣金结算、个人服务费佣金结算、个人奖金结算、成员合并结算、提现申请、提现打款、交押金、退押金、退款)")
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

    @ApiModelProperty("钱包累计入账(分)_变动金额")
    private Long accumulateIncomeChange;

    @ApiModelProperty("钱包累计入账(分)_变动后")
    private Long accumulateIncomeAfter;

    @ApiModelProperty("'奖金累计收入-变动前'(分)")
    private Long accAwardIncomeBefore;

    @ApiModelProperty("'奖金累计收入-变动金额'(分)")
    private Long accAwardIncomeChange;

    @ApiModelProperty("'奖金累计收入-变动后'(分)")
    private Long accAwardIncomeAfter;

    @ApiModelProperty("'累计提现金额-变动前'(分)")
    private Long accWithdrawBefore;

    @ApiModelProperty("'累计提现金额-变动金额'(分)")
    private Long accWithdrawChange;

    @ApiModelProperty("'累计提现金额-变动后'(分)")
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
