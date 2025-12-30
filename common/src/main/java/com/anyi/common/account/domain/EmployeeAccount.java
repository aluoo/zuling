package com.anyi.common.account.domain;

import com.anyi.common.domain.entity.BaseEntity;
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
 * 个人账户表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Getter
@Setter
@TableName("employee_account")
@ApiModel(value = "EmployeeAccount对象", description = "个人账户表")
public class EmployeeAccount extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("钱包可用余额(分)")
    private Long ableBalance;

    @ApiModelProperty("钱包临时冻结金额(分)")
    private Long tempFrozenBalance;

    @ApiModelProperty("钱包永久冻结金额(分)")
    private Long frozenBalance;

    @ApiModelProperty("钱包累计入账(分)")
    private Long accumulateIncome;

    @ApiModelProperty("团队奖金累计入账(分)")
    private Long accAwardIncome;

    @ApiModelProperty("累计提现(分)")
    private Long accWithdraw;

    @ApiModelProperty("组织层级编码")
    private String ancestors;

    @ApiModelProperty("是否子后台提现(0-否,1-是)")
    private Boolean inventedWithdrawFlag;

}
