package com.anyi.common.commission.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统结算单
 * </p>
 *
 * @author chenjian
 * @since 2023-06-20
 */
@Getter
@Setter
@TableName("commission_settle_check")
public class CommissionSettleCheck extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("合伙人ID")
    private Long bdId;

    @ApiModelProperty("区域经理ID")
    private Long regionId;

    @ApiModelProperty("分佣方案ID")
    private Long commissionType;

    @ApiModelProperty("分佣套餐ID")
    private Long commissionPackage;

    @ApiModelProperty("结算金额（分）")
    private Integer settleBalance;

    @ApiModelProperty("付出金额（分）")
    private Integer payAmount;

    @ApiModelProperty("总金额（分）")
    private Integer allAmount;

    @ApiModelProperty("结算关联扩展id")
    private Long correlationId;

    @ApiModelProperty("结算时间")
    private Date settleTime;

    @ApiModelProperty("结算说明")
    private String remark;

}
