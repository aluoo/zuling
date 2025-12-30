package com.anyi.common.product.domain.response;

import com.anyi.common.util.MoneyUtil;
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
 * @Date 2024/3/4
 * @Copyright
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("客户申请退款信息响应对象")
public class RefundPaymentInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单信息&收款信息")
    private ReceivePaymentInfoVO order;

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
    @ApiModelProperty(value = "跳转连接二维码图片地址")
    private String qrCodeUrl;
    @ApiModelProperty("退款原因")
    private String reason;

    public void setAmountStr() {
        if (this.amount != null && this.amount > 0) {
            this.amountStr = MoneyUtil.fenToYuan(this.amount);
        }
    }
}