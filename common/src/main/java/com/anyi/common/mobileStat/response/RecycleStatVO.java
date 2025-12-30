package com.anyi.common.mobileStat.response;

import com.anyi.common.domain.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 门店统计日看板表
 * </p>
 *
 * @author L
 * @since 2024-03-08
 */
@Data
public class RecycleStatVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("二手机交易金额")
    private Long transAmount;

    @ApiModelProperty("二手机交易金额")
    private String transAmountStr;

    @ApiModelProperty("二手机交易数量")
    private Integer transNum;

    @ApiModelProperty("二手机平均交易金额元")
    private String avgTransAmountStr;

    @ApiModelProperty("二手机退款金额")
    private Long refundAmount;

    @ApiModelProperty("二手机退款金额元")
    private String refundAmountStr;

    @ApiModelProperty("二手机出价次数")
    private Integer quotePriceNum;

    @ApiModelProperty("二手机报价时长(毫秒)")
    private Long quoteTimeSpent;

    @ApiModelProperty("二手机报价时长分")
    private String quoteTimeSpentStr;

    @ApiModelProperty("二手机报价时长分")
    private String avgQuoteTimeSpentStr;

    @ApiModelProperty("二手机确认收货量")
    private Integer orderConfirmNum;


}
