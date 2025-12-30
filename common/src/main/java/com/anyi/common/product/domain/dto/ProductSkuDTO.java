package com.anyi.common.product.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/28
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty(value = "是否上架")
    private Boolean activated;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "规格名称")
    private String spec;
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "手机商品ID")
    private Long productId;
    @ApiModelProperty(value = "市场零售价")
    private Integer retailPrice;
    private String retailPriceStr;
    @ApiModelProperty(value = "1安卓 2苹果")
    private Integer type;
    @ApiModelProperty(value = "是否为pro机型 0否1是")
    private Integer proStatus;
}