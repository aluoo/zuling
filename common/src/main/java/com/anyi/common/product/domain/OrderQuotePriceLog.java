package com.anyi.common.product.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.enums.OrderQuoteLogStatusEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogSubStatusEnum;
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
import java.math.BigDecimal;
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
@TableName("mb_order_quote_price_log")
@ApiModel(value = "回收商报价记录表")
public class OrderQuotePriceLog extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "报价订单ID")
    private Long orderId;
    @ApiModelProperty(value = "回收商员工ID")
    private Long employeeId;
    @ApiModelProperty(value = "回收商ID")
    private Long companyId;
    @ApiModelProperty(value = "原始报价")
    private Integer originalQuotePrice;
    @ApiModelProperty(value = "成交价")
    private Integer finalPrice;
    @ApiModelProperty(value = "实际付款价格=原始报价+平台抽成金额")
    private Integer actualPaymentPrice;
    @ApiModelProperty(value = "平台抽成规则")
    private BigDecimal platformCommissionRule;
    @ApiModelProperty(value = "平台抽成金额")
    private Integer platformCommission;
    @ApiModelProperty(value = "门店抽成规则")
    private BigDecimal commissionRule;
    @ApiModelProperty(value = "门店抽成金额")
    private Integer commission;
    @ApiModelProperty("平台补贴")
    private Integer platformSubsidyPrice;
    @ApiModelProperty(value = "是否已报价，默认为否")
    private Boolean quoted;
    @ApiModelProperty(value = "报价时间")
    private Date quoteTime;
    @ApiModelProperty(value = "总体报价用时=报价时间-报价订单创建时间 单位毫秒")
    private Long quoteTimeSpent;
    @ApiModelProperty(value = "实际报价用时=报价时间-抢单时间 单位毫秒 单位毫秒")
    private Long quoteTimeSpentReal;

    /**
     * @see OrderQuoteLogStatusEnum
     */
    @ApiModelProperty(value = "报价状态")
    private Integer status;
    /**
     * @see OrderQuoteLogSubStatusEnum
     */
    @ApiModelProperty(value = "报价子状态")
    private Integer subStatus;
}