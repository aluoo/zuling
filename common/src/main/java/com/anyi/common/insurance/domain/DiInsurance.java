package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 保险产品表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_insurance")
@ApiModel(value = "DiInsurance对象", description = "保险产品表")
public class DiInsurance extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("险种ID")
    private Long typeId;

    @ApiModelProperty("套餐ID")
    private Long packageId;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty("产品描述")
    private String description;

    @ApiModelProperty("年限")
    private Integer period;

    @ApiModelProperty("1正常2下线3删除")
    private Integer status;
}