package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 手机-数保产品价格基础配置表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_product_base_config")
@ApiModel(value = "DiProductBaseConfig对象", description = "手机-数保产品价格基础配置表")
public class DiProductBaseConfig extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("手机ID")
    private Long productId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("保险产品ID")
    private Long insuranceId;

    @ApiModelProperty("险种类型ID")
    private Long typeId;

    @ApiModelProperty("套餐ID")
    private Long packageId;

    @ApiModelProperty("保险产品保额")
    private Long insuranceAmount;

    @ApiModelProperty("平台底价")
    private Long basePrice;

    @ApiModelProperty("建议零售价")
    private Long retailPriceSuggested;

    @ApiModelProperty("封顶零售价")
    private Long retailPriceMax;

    @ApiModelProperty("A类门店成本价")
    private Long costPriceA;

    @ApiModelProperty("B类门店成本价")
    private Long costPriceB;

    @ApiModelProperty("C类门店成本价")
    private Long costPriceC;
}
