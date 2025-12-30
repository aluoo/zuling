package com.anyi.common.product.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/31
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品规格响应对象")
public class ProductSkuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("SKU ID")
    private Long id;
    @ApiModelProperty("规格名称")
    private String spec;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer retailPrice;
    @ApiModelProperty(value = "市场零售价")
    private String retailPriceStr;
}