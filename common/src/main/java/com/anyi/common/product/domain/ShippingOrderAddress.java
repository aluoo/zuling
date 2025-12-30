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

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mb_shipping_order_address")
@ApiModel(value = "发货订单收寄地址信息表")
public class ShippingOrderAddress extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;
    @ApiModelProperty(value = "发货订单ID")
    private Long shippingOrderId;
    @ApiModelProperty("1寄出方 2收货方")
    private Integer type;
    @ApiModelProperty("省市区")
    private String address;
    @ApiModelProperty("详细地址")
    private String detail;
    @ApiModelProperty("联系人")
    private String contact;
    @ApiModelProperty("电话")
    private String phone;
}