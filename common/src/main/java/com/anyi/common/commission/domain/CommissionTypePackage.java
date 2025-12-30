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
@TableName("commission_type_package")
@ApiModel(value = "CommissionTypePackage对象", description = "佣金方案套餐")
public class CommissionTypePackage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long typeId;

    @ApiModelProperty("1数值2比例")
    private Integer type;

    @ApiModelProperty("方案类型名称")
    private String name;

    @ApiModelProperty("数值分佣金额上限")
    private Long maxCommissionFee;
    private Integer deleted;
}
