package com.anyi.common.insurance.response;

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
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("数保订单提交响应对象")
public class InsuranceOrderApplyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    private Long orderId;
}