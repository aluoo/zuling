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
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mb_order")
@ApiModel(value = "报价订单表")
public class Order extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "门店员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty(value = "门店ID")
    private Long storeCompanyId;
    @ApiModelProperty(value = "商品ID")
    private Long productId;
    @ApiModelProperty(value = "商品名称")
    private String productName;
    @ApiModelProperty(value = "订单编码")
    private String orderNo;
    /**
     * @see com.anyi.common.product.domain.enums.OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    @ApiModelProperty(value = "IMEI号")
    private String imeiNo;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "原始报价")
    private Integer originalQuotePrice;
    @ApiModelProperty(value = "成交价")
    private Integer finalPrice;
    @ApiModelProperty(value = "门店抽成金额")
    private Integer commission;
    @ApiModelProperty(value = "平台补贴价格")
    private Integer platformSubsidyPrice;
    @ApiModelProperty(value = "回收商员工ID")
    private Long recyclerEmployeeId;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;
    @ApiModelProperty(value = "确认报价详情ID")
    private Long quotePriceLogId;
    @ApiModelProperty(value = "确认报价时间")
    private Date finishQuoteTime;

    @ApiModelProperty(value = "是否绑码")
    private Boolean bound;
    @ApiModelProperty(value = "是否核验")
    private Boolean verified;
    @ApiModelProperty(value = "是否可报价（超时将关闭报价功能）")
    private Boolean quotable;

    /**
     * 由确认收款的时间节点开始计算，单笔交易额大于等于200，则超时时间为当天结束
     * 若小于200，则超时时间为本周末结束
     */
    @ApiModelProperty(value = "发货超时时间")
    private Date shippingOverdueTime;
    /**
     * 回收商对发货订单进行确认收货后，更新关联的所有订单的收货时间
     */
    @ApiModelProperty(value = "确认收货时间")
    private Date confirmReceiptTime;

    @ApiModelProperty(value = "是否可申请退款")
    private Boolean canApplyRefund;
}