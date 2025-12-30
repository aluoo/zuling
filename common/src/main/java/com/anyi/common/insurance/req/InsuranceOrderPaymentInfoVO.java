package com.anyi.common.insurance.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/7
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("数保订单微信支付二维码信息响应对象")
public class InsuranceOrderPaymentInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "跳转连接二维码图片地址")
    private String qrCodeUrl;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer amount;
    @ApiModelProperty("金额")
    private String amountStr;
    @ApiModelProperty("支付状态 0待支付 1已支付")
    private Integer status;
    @ApiModelProperty("支付时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    @ApiModelProperty("数保产品名称")
    private String productName;
    @ApiModelProperty("数保产品规格")
    private String productSpec;

    @ApiModelProperty("数保产品名称")
    private String insuranceName;
    @ApiModelProperty("数保产品类型")
    private String insuranceType;
    @ApiModelProperty("数保产品年限")
    private Integer insurancePeriod;

}