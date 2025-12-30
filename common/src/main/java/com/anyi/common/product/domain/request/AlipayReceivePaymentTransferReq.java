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
 * @Date 2024/3/5
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("支付宝收款请求对象")
public class AlipayReceivePaymentTransferReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付宝authCode")
    @NotBlank(message = "code不能为空")
    private String authorizationCode;
    @ApiModelProperty("报价订单号")
    @NotNull(message = "订单号不能为空")
    private Long orderId;
    @ApiModelProperty("收款单号")
    @NotNull(message = "订单号不能为空")
    private Long paymentId;
}