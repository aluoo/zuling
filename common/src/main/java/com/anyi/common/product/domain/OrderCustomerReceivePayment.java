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
@TableName("mb_order_customer_receive_payment")
@ApiModel(value = "报价订单-用户收款信息表（平台打钱给用户）")
public class OrderCustomerReceivePayment extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "报价订单ID")
    private Long orderId;
    @ApiModelProperty(value = "支付宝商户订单号")
    private String outBizNo;
    @ApiModelProperty(value = "收款人姓名")
    private String name;
    @ApiModelProperty(value = "收款人手机号")
    private String mobile;
    @ApiModelProperty(value = "收款人身份证")
    private String idCard;
    @ApiModelProperty(value = "收款金额")
    private Integer amount;
    @ApiModelProperty(value = "收款状态 0待收款 1已收款")
    private Integer status;
    @ApiModelProperty(value = "收款方式")
    private Integer type;
    @ApiModelProperty(value = "收款时间")
    private Date receiveTime;
    @ApiModelProperty(value = "支付宝openId")
    private String openId;
    @ApiModelProperty(value = "支付宝转账订单号")
    private String alipayOrderId;
    @ApiModelProperty(value = "支付宝支付资金流水号")
    private String payFundOrderId;
    @ApiModelProperty(value = "跳转连接二维码图片地址")
    private String qrCodeUrl;
    @ApiModelProperty(value = "接口返回日志")
    private String remoteResp;
}