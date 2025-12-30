package com.anyi.common.product.domain.response;

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
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("回收商报价价格信息响应对象")
public class RecyclerQuotePriceInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报价记录ID")
    private Long orderQuoteLogId;

    @ApiModelProperty("报价金额")
    private String price;
    @ApiModelProperty("平台服务费")
    private String platformCommission;
    @ApiModelProperty("平台服务费比例")
    private String platformCommissionRule;
    @ApiModelProperty("预警金额")
    private String warningThresholdPrice;
    @ApiModelProperty("是否提示超过预警金额")
    private Boolean warning;
}