package com.anyi.common.commission.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分佣方案记录表;
 *
 * @author : Jianpan
 * @date : 2023-3-28
 */
@Data
@ApiModel(value = "分佣方案记录表", description = "")
@TableName("commission_plan_log")
public class CommissionPlanLog extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", notes = "")
    private Long id;

    @ApiModelProperty(name = "方案id")
    private Long planId;

    @ApiModelProperty(name = "创建人id")
    private Long employeeId;

    @ApiModelProperty(name = "方案内容（方案，成员，配置）")
    private String content;
}
