package com.anyi.common.product.domain.dto;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/12
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteCommissionInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报价金额")
    private Integer price;

    @ApiModelProperty("平台补贴")
    private Integer platformSubsidyPrice;
    @ApiModelProperty("实际分佣比例")
    private BigDecimal realCommissionScale;
    @ApiModelProperty("实际最大分佣金额(单位：分)")
    private Long realCommissionFee;

    private BigDecimal actualCommissionRule;
    private Integer actualCommission;

    public String getActualCommissionRuleFormat() {
        if (this.actualCommissionRule == null) {
            return null;
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.mul(actualCommissionRule, 100));
    }
}