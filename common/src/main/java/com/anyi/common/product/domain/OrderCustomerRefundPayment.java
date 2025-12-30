package com.anyi.common.product.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
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
@TableName("mb_order_customer_refund_payment")
@ApiModel(value = "报价订单-用户退款信息表（用户主动支付退款给平台）")
public class OrderCustomerRefundPayment extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "报价订单ID")
    private Long orderId;
    @ApiModelProperty(value = "收款订单ID")
    private Long receivePaymentId;
    @ApiModelProperty(value = "用户openId")
    private String openId;
    @ApiModelProperty(value = "平台交易订单号")
    private String outTradeNo;
    @ApiModelProperty(value = "微信支付订单号")
    private String transactionId;
    @ApiModelProperty(value = "收款金额")
    private Integer amount;
    @ApiModelProperty(value = "收款状态 0待支付 1已支付")
    private Integer status;
    @ApiModelProperty(value = "支付方式")
    private Integer type;
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
    @ApiModelProperty(value = "跳转连接二维码图片地址")
    private String qrCodeUrl;
    @ApiModelProperty(value = "退款原因")
    private String reason;
}