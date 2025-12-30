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
 * 佣金方案表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("commission_plan")
@ApiModel(value = "CommissionPlan对象", description = "佣金方案表")
public class CommissionPlan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("方案名称(可为空)")
    private String name;

    @ApiModelProperty("佣金方案ID")
    private Long typeId;

    @ApiModelProperty("创建来源(0-后台、1-app)")
    private Byte sourceType;

    @ApiModelProperty("创建员工id")
    private Long employeeId;

    @ApiModelProperty("创建后台人员id")
    private Long sysUserId;

    @ApiModelProperty("层级(从0开始)")
    private Integer level;

    @ApiModelProperty("是否叶子节点(0-不是,1-是)")
    private Boolean isLeaf;

}
