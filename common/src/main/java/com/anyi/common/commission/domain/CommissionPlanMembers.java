package com.anyi.common.commission.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 佣金方案-套餐佣金配置-成员关系表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("commission_plan_members")
@ApiModel(value = "CommissionPlanMembers对象", description = "佣金方案-套餐佣金配置-成员关系表")
public class CommissionPlanMembers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("方案id")
    private Long planId;

    @ApiModelProperty("佣金方案类型")
    private Long typeId;

    @ApiModelProperty("下级员工id")
    private Long childEmployeeId;

}
