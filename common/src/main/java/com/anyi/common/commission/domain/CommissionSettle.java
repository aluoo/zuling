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
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 系统结算单
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("commission_settle")
@ApiModel(value = "CommissionSettle对象", description = "系统结算单")
public class CommissionSettle extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("结算批次号")
    private Long batchNo;

    @ApiModelProperty("分佣方案ID")
    private Long commissionType;

    @ApiModelProperty("分佣套餐ID")
    private Long commissionPackage;

    @ApiModelProperty("结算金额（分）")
    private Integer settleBalance;


    @ApiModelProperty("结算状态(0-不结算、1-待结算、2-已结算)")
    private Integer settleStatus;

    @ApiModelProperty("结算关联扩展id")
    private Long correlationId;

    @ApiModelProperty("获得类型(1-直接做单获得,2-下级贡献)")
    private Integer gainType;

    @ApiModelProperty("获得时间")
    private Date gainTime;

    @ApiModelProperty("结算时间")
    private Date settleTime;

    @ApiModelProperty("佣金来源的员工id")
    private Long childEmployeeId;

    @ApiModelProperty("结算说明")
    private String remark;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("组织层级编码")
    private String ancestors;
}
