package com.anyi.common.insurance.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiProductInsurancePriceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("数保产品ID")
    private Long insuranceId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("门店初始售价(单位：分)")
    private Integer salePrice;

    @ApiModelProperty("门店进货价格(单位：分)")
    private Integer normalPrice;

    @ApiModelProperty("价格区间下限(单位：分)")
    private Integer priceLow;

    @ApiModelProperty("价格区间上限(单位：分)")
    private Integer priceHigh;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;

    private Integer queryPrice;
}