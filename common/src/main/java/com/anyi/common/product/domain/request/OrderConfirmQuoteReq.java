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
 * @Date 2024/2/28
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("报价订单确认交易请求对象")
public class OrderConfirmQuoteReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报价订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long id;
    @ApiModelProperty(value = "确认报价详情ID")
    @NotNull(message = "报价信息不能为空")
    private Long quotePriceLogId;
    @ApiModelProperty(value = "IMEI号")
    @NotBlank(message = "IMEI号不能为空")
    private String imeiNo;
}