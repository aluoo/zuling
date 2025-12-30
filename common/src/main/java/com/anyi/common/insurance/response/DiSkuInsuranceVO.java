package com.anyi.common.insurance.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
public class DiSkuInsuranceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("品牌型号")
    private String productName;

    @ApiModelProperty("市场零售价")
    private String retailPrice;

    @ApiModelProperty("产品明细")
    private List<insuranceVO> insuranceList;

    @Data
    public static class insuranceVO implements Serializable{
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("数保产品名称")
        private String insuranceName;

        @ApiModelProperty("产品描述")
        private String description;

        @ApiModelProperty("数保年限0终身")
        private Integer insurancePeriod;

        @ApiModelProperty("平台成本")
        private String downPrice;

        @ApiModelProperty("门店成本")
        private String normalPrice;

        @ApiModelProperty("收益")
        private String benefit;

    }
}