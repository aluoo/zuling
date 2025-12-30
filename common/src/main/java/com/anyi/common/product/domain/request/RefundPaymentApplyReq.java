package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户申请退款信息请求对象")
public class RefundPaymentApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报价订单号")
    @NotNull(message = "订单号不能为空")
    private Long id;
    @ApiModelProperty("退款原因")
    @NotBlank(message = "退款原因不能为空")
    private String reason;
}