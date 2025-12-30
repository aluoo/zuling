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
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("物流下单前置信息查询请求对象")
public class PreCreateLogisticsReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货订单号")
    @NotNull(message = "订单号不能为空")
    private Long shippingOrderId;

    @ApiModelProperty("寄件类型 1线上 2线下")
    @NotNull(message = "寄件类型不能为空")
    private Integer shippingType;
}