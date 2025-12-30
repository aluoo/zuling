package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.enums.DiOrderPayStatusEnum;
import com.anyi.common.insurance.enums.DiOrderPayTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("di_insurance_order_payment")
@ApiModel(value = "数保支付订单表")
public class DiInsuranceOrderPayment extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "数保订单ID")
    private Long insuranceOrderId;
    @ApiModelProperty(value = "用户openId")
    private String openId;
    @ApiModelProperty(value = "平台交易订单号")
    private String outTradeNo;
    @ApiModelProperty(value = "微信支付订单号")
    private String transactionId;
    @ApiModelProperty(value = "平台退款订单号")
    private String refundNo;
    @ApiModelProperty(value = "微信退款订单号")
    private String refundId;
    @ApiModelProperty(value = "金额")
    private Integer amount;
    /**
     * @see DiOrderPayStatusEnum
     */
    @ApiModelProperty(value = "状态 0待支付 1已支付 2退款中 3已退款")
    private Integer status;
    /**
     * @see DiOrderPayTypeEnum
     */
    @ApiModelProperty(value = "支付方式")
    private Integer type;
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
    @ApiModelProperty(value = "跳转连接二维码图片地址")
    private String qrCodeUrl;
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;
    @ApiModelProperty(value = "退款原因")
    private String refundReason;
    @ApiModelProperty(value = "退款备注")
    private String refundRemark;
}