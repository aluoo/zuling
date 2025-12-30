package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/2
 * @Copyright
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单报价信息列表查询请求对象")
public class OrderQuoteQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报价订单号")
    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @ApiModelProperty(value = "报价信息ID", hidden = true)
    private Long id;

    @ApiModelProperty(hidden = true)
    private Long recyclerCompanyId;
}