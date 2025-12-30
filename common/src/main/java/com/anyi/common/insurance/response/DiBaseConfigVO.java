package com.anyi.common.insurance.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 手机-数保产品价格基础配置表
 * </p>
 *
 * @author L
 * @since 2024-05-29
 */
@Getter
@Setter
public class DiBaseConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyBaseId;

    @ApiModelProperty("手机ID")
    private Long productId;

    @ApiModelProperty("保险产品ID")
    private Long insuranceId;

    @ApiModelProperty("险种类型ID")
    private Long typeId;

    @ApiModelProperty("险种类型名称")
    private String typeName;

    @ApiModelProperty("套餐ID")
    private Long packageId;

    @ApiModelProperty("套餐名称")
    private String packageName;

    @ApiModelProperty("保险产品保额")
    private Long insuranceAmount;

    @ApiModelProperty("平台底价")
    private Long basePrice;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("品牌ID")
    private Long brandId;

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
