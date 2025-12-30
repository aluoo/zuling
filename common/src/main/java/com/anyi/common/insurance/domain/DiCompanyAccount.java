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
 * 数保门店账户表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_company_account")
@ApiModel(value = "DiCompanyAccount对象", description = "数保门店账户表")
public class DiCompanyAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("门店ID")
    private Long companyId;

    @ApiModelProperty("钱包可用余额(分)")
    private Long ableBalance;

    @ApiModelProperty("钱包临时冻结金额(分)")
    private Long tempFrozenBalance;

    @ApiModelProperty("钱包永久冻结金额(分)")
    private Long frozenBalance;

    @ApiModelProperty("钱包累计入账(分)")
    private Long accumulateIncome;

    @ApiModelProperty("奖金累计收入")
    private Long accAwardIncome;

    @ApiModelProperty("累计提现金额")
    private Long accWithdraw;

    @ApiModelProperty("组织层级编码")
    private String ancestors;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
