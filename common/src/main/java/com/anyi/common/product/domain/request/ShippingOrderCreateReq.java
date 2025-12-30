package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/7
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("发货订单创建请求接口")
public class ShippingOrderCreateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "需要发货的订单ID列表", required = true)
    @Valid
    @NotEmpty(message = "订单ID不能为空")
    private List<Long> orderIds;

    @ApiModelProperty(value = "回收商ID", required = true)
    @NotNull(message = "回收商ID不能为空")
    private Long companyId;
}