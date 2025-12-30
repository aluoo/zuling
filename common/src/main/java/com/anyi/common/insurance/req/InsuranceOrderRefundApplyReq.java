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
@ApiModel("数保订单退款申请请求对象")
public class InsuranceOrderRefundApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单号不能为空")
    public Long orderId;

    @ApiModelProperty("原因")
    private String reason;
    @ApiModelProperty("备注")
    private String remark;
}