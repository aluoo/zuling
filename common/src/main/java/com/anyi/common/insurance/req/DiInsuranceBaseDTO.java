package com.anyi.common.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class DiInsuranceBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("基础配置数组")
    List<InsuranceBaseDetailDTO> baseList;

    @ApiModelProperty("后端自己传")
    private Long companyId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InsuranceBaseDetailDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long id;

        private Long companyBaseId;

        @ApiModelProperty("手机产品ID")
        @NotNull(message = "手机产品ID不能为空")
        private Long productId;

        @ApiModelProperty("数保产品ID")
        @NotNull(message = "数保产品ID不能为空")
        private Long insuranceId;

        @ApiModelProperty("保险产品保额")
        @NotNull(message = "保险产品保额不能为空")
        private Long insuranceAmount;

        @ApiModelProperty("平台底价")
        @NotNull(message = "平台底价不能为空")
        private Long basePrice;

        @ApiModelProperty("建议零售价")
        @NotNull(message = "建议零售价不能为空")
        private Long retailPriceSuggested;

        @ApiModelProperty("封顶零售价")
        @NotNull(message = "封顶零售价不能为空")
        private Long retailPriceMax;

        @ApiModelProperty("A类门店成本价")
        @NotNull(message = "A类门店成本价不能为空")
        private Long costPriceA;

        @ApiModelProperty("B类门店成本价")
        @NotNull(message = "B类门店成本价不能为空")
        private Long costPriceB;

        @ApiModelProperty("C类门店成本价")
        @NotNull(message = "C类门店成本价不能为空")
        private Long costPriceC;


    }


}