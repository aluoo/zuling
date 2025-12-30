package com.anyi.common.insurance.response;

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
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("手机规格-保险产品信息响应对象")
public class DiInsuranceInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("保险产品ID")
    private Long id;

    @ApiModelProperty("保险产品名称")
    private String name;

    @ApiModelProperty("年限 0为终身")
    private Integer period;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer salePrice;
    @ApiModelProperty("建议零售价")
    private String salePriceStr;

    @ApiModelProperty("保险产品类型")
    private String type;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer normalPrice;
    @ApiModelProperty("门店进货价格")
    private String normalPriceStr;
}