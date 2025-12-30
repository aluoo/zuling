package com.anyi.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName("wm_pay_callback_record")
public class PayCallbackRecord {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "微信回调原始数据")
    private String originalInfo;

    @ApiModelProperty(value = "实际商家")
    private String actualMchid;

    @ApiModelProperty(value = "支付入口")
    private String payEnter;

    @ApiModelProperty(value = "回调解密后的数据")
    private String decryptedInfo;
    @ApiModelProperty(value = "商户系统内部的订单号")
    private String outTradeNo;

    private Date createTime;
}