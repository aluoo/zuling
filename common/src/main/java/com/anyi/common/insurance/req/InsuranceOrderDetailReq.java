package com.anyi.common.insurance.req;

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
 * @Date 2024/2/23
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("数保报修投保订单详情对象")
public class InsuranceOrderDetailReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("投保订单Id")
    @NotNull(message = "投保订单Id")
    public Long orderId;

    @ApiModelProperty("数保报修服务类型ID,care+的时候传")
    public Long optionId;

}