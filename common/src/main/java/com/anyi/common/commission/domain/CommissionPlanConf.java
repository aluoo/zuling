package com.anyi.common.commission.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 佣金配置表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("commission_plan_conf")
@ApiModel(value = "CommissionPlanConf对象", description = "佣金配置表")
public class CommissionPlanConf extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("方案id")
    private Long planId;

    @ApiModelProperty("佣金套餐id")
    private Long typePackageId;

    @ApiModelProperty("上级分成")
    private Long superDivide;

    @ApiModelProperty("下级分成")
    private Long childDivide;

    @ApiModelProperty("自收分成")
    private Long selfDivide;

    @ApiModelProperty("上级分成比例")
    private BigDecimal superScale;

    @ApiModelProperty("下级分成比例")
    private BigDecimal childScale;

    @ApiModelProperty("自收分成比例")
    private BigDecimal selfScale;

    @ApiModelProperty("组织层级编码")
    private String ancestors;

    @ApiModelProperty("层级")
    private Integer level;

}
