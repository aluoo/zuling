package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

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
@ApiModel("回收商报价请求对象")
public class RecyclerQuoteReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报价记录ID")
    @NotNull(message = "ID不能为空")
    private Long orderQuoteLogId;

    @ApiModelProperty("报价金额")
    @NotNull(message = "报价金额不能为空")
    @Min(value = 1, message = "报价金额不能小于1")
    @Positive(message = "报价金额不能小于1")
    private BigDecimal price;

    @ApiModelProperty("备注")
    private String quoteRemark;
}