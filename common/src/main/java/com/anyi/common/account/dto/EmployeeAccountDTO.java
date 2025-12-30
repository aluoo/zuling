package com.anyi.common.account.dto;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 个人账户表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Data
public class EmployeeAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("钱包可用余额(元)")
    private String ableBalanceStr;

    @ApiModelProperty("钱包临时冻结金额(元)")
    private Long tempFrozenBalanceStr;

    @ApiModelProperty("钱包永久冻结金额(元)")
    private Long frozenBalanceStr;

    @ApiModelProperty("钱包累计入账(元)")
    private Long accumulateIncomeStr;

    @ApiModelProperty("团队奖金累计入账(元)")
    private Long accAwardIncomeStr;

    @ApiModelProperty("累计提现(元)")
    private Long accWithdrawStr;

}
