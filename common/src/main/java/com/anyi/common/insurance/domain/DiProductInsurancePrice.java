package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * 数保产品售价价格区间设置
 * </p>
 *
 * @author L
 * @since 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("di_product_insurance_price")
@ApiModel(value = "DiProductInsurancePrice对象", description = "数保产品售价价格区间设置")
public class DiProductInsurancePrice extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("数保产品ID")
    private Long insuranceId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("门店进货价格(单位：分)")
    private Integer normalPrice;

    @ApiModelProperty("门店初始售价(单位：分)")
    private Integer salePrice;

    @ApiModelProperty("价格区间下限(单位：分)")
    private Integer priceLow;

    @ApiModelProperty("价格区间上限(单位：分)")
    private Integer priceHigh;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;
}