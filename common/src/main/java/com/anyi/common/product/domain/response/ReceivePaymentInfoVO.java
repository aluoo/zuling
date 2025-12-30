package com.anyi.common.product.domain.response;

import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("客户收款信息响应对象")
public class ReceivePaymentInfoVO extends OrderBaseVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("收款人姓名")
    private String name;
    @ApiModelProperty("收款人手机")
    private String mobile;
    @ApiModelProperty("收款人身份证")
    private String idCard;
    @ApiModelProperty(value = "收款金额", hidden = true)
    @JsonIgnore
    private Integer amount;
    @ApiModelProperty("收款金额")
    private String amountStr;
    @ApiModelProperty("收款状态 0待收款 1已收款")
    private Integer receiveStatus;
    @ApiModelProperty("收款时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;
    @ApiModelProperty(value = "跳转连接二维码图片地址")
    private String qrCodeUrl;

    public void setAmountStr() {
        if (this.amount != null && this.amount > 0) {
            this.amountStr = MoneyUtil.fenToYuan(this.amount);
        }
    }

    public void setReceivePaymentInfo(OrderCustomerReceivePayment payment) {
        if (payment != null) {
            this.setName(payment.getName());
            this.setMobile(payment.getMobile());
            this.setIdCard(payment.getIdCard());
            this.setReceiveStatus(payment.getStatus());
            this.setReceiveTime(payment.getReceiveTime());
            this.setQrCodeUrl(payment.getQrCodeUrl());
        }
    }
}