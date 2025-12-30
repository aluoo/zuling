package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@TableName("di_sku_insurance")
@ApiModel(value = "数保产品SKU底价表")
public class DiSkuInsurance extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("SKU ID")
    private Long skuId;

    @ApiModelProperty("数保产品ID")
    private Long insuranceId;

    @ApiModelProperty("sku底价")
    private Long downPrice;
}