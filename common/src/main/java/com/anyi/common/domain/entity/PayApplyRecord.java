package com.anyi.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("wm_pay_apply_record")
public class PayApplyRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty(value = "实际商家")
    private String actualMchId;

    @ApiModelProperty(value = "支付入口")
    private String payEnter;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty(value = "商户系统内部的订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "业务类型")
    private Integer bizType;
}