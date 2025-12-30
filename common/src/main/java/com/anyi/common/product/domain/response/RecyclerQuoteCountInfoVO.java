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
@ApiModel("回收商报价中心报价单数量统计响应对象")
public class RecyclerQuoteCountInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回收商ID")
    private Long recyclerCompanyId;

    @ApiModelProperty("待报价数量")
    @Builder.Default
    private Integer pendingQuoteCount = 0;

    @ApiModelProperty("报价中数量")
    @Builder.Default
    private Integer quotingCount = 0;

    @ApiModelProperty("已报价数量")
    @Builder.Default
    private Integer quotedCount = 0;

    @ApiModelProperty("已作废数量")
    @Builder.Default
    private Integer canceledCount = 0;
}