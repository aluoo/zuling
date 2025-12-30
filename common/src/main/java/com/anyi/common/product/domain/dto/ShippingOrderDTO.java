package com.anyi.common.product.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/9
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Boolean deleted;

    @ApiModelProperty(value = "门店发货员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty(value = "门店ID")
    private Long storeCompanyId;
    @ApiModelProperty(value = "回收商确认收货员工ID")
    private Long recyclerEmployeeId;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "物流下单时间")
    private Date applyLogisticsTime;
    @ApiModelProperty(value = "确认收货时间")
    private Date confirmReceiptTime;

    @ApiModelProperty("寄件类型 1线上 2线下")
    private Integer shippingType;

    @ApiModelProperty("快递公司编码")
    private String trackCompanyCode;
    @ApiModelProperty("快递公司名称")
    private String trackCompanyName;

    @ApiModelProperty("快递物流单号")
    private String trackNo;

    @ApiModelProperty("期望揽收开始时间")
    private Date pickupStartTime;
    @ApiModelProperty("期望揽收结束时间")
    private Date pickupEndTime;

    @ApiModelProperty("实际物流运费 单位分")
    private Integer price;
    @ApiModelProperty("物流月结折扣费率")
    private BigDecimal discountRate;
    @ApiModelProperty("折扣金额 = 原始物流费用-实际物流费用 单位分")
    private Integer discountPrice;
    @ApiModelProperty("折扣前原始物流费用 = 实际物流费用 / 折扣费率 单位分")
    private Integer originalPrice;

    private Long orderId;
}