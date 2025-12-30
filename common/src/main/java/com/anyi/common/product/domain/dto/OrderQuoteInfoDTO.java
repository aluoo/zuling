package com.anyi.common.product.domain.dto;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.product.domain.enums.OrderQuoteLogStatusEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogSubStatusEnum;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @Date 2024/3/2
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderQuoteInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报价记录ID")
    private Long id;
    @ApiModelProperty(value = "报价订单ID")
    private Long orderId;
    @ApiModelProperty(value = "回收商ID")
    private Long companyId;
    @ApiModelProperty(value = "回收商名称")
    private String companyName;
    @ApiModelProperty(value = "报价师ID")
    private Long employeeId;
    @ApiModelProperty(value = "报价师名称")
    private String employeeName;
    @ApiModelProperty(value = "报价师电话")
    private String employeeMobile;
    @ApiModelProperty(value = "报价订单创建时间", hidden = true)
    @JsonIgnore
    private Date orderCreateTime;
    @ApiModelProperty(value = "报价用时=报价订单创建时间-报价时间 单位毫秒")
    @JsonIgnore
    private Long quoteTimeSpent;
    @ApiModelProperty(value = "实际报价用时=报价时间-抢单时间 单位毫秒 单位毫秒", hidden = true)
    @JsonIgnore
    private Long quoteTimeSpentReal;
    @ApiModelProperty(value = "报价提交时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quoteTime;
    @ApiModelProperty(value = "总体报价用时", example = "15分41秒")
    private String timeSpent;
    @ApiModelProperty(value = "实际报价用时", example = "15分41秒")
    private String timeSpentReal;
    @ApiModelProperty(value = "是否已报价")
    private Boolean quoted;

    @ApiModelProperty(value = "原始报价", hidden = true)
    @JsonIgnore
    private Integer originalQuotePrice;
    @ApiModelProperty(value = "原始报价")
    private String originalQuotePriceStr;
    @ApiModelProperty(value = "成交价格", hidden = true)
    @JsonIgnore
    private Integer finalPrice;
    @ApiModelProperty(value = "成交价格 (最终确认的报价，用户实际收款的价格，用户退款的价格，回收商报价的价格)")
    private String finalPriceStr;
    @ApiModelProperty(value = "门店压价", hidden = true)
    @JsonIgnore
    private Integer commission;
    @ApiModelProperty(value = "门店压价")
    private String commissionStr;

    @ApiModelProperty(value = "平台补贴价格", hidden = true)
    @JsonIgnore
    private Integer platformSubsidyPrice;
    @ApiModelProperty(value = "平台补贴价格")
    private String platformSubsidyPriceStr;

    /**
     * @see OrderQuoteLogStatusEnum
     */
    @ApiModelProperty(value = "报价状态 0待报价 1报价中 2已报价 -1已作废")
    private Integer status;
    /**
     * @see OrderQuoteLogSubStatusEnum
     */
    @ApiModelProperty(value = "报价子状态 0待报价 1报价中 2已确认交易 3未入选 4超时未报价 5超时未确认交易 6交易取消 7交易退款 8拒绝报价")
    private Integer subStatus;

    @ApiModelProperty(value = "是否可以取消报价，报价中且自己的报价单才能取消")
    private Boolean canCancel;

    @ApiModelProperty(value = "是否可以抢单")
    private Boolean canLock;

    @ApiModelProperty(value = "是否可以报价")
    private Boolean canQuote;

    @ApiModelProperty(value = "回收商评分", hidden = true)
    private Object recyclerRate;

    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;

    @ApiModelProperty(value = "报价备注")
    private String quoteRemark;

    public void setOriginalPriceStr() {
        if (this.getOriginalQuotePrice() != null && this.getOriginalQuotePrice() >= 0) {
            this.originalQuotePriceStr = MoneyUtil.fenToYuan(this.getOriginalQuotePrice());
        }
    }

    public void setFinalPriceStr() {
        if (this.getFinalPrice() != null && this.getFinalPrice() >= 0) {
            this.finalPriceStr = MoneyUtil.fenToYuan(this.getFinalPrice());
        }
    }

    public void setCommissionStr() {
        if (this.getCommission() != null && this.getCommission() >= 0) {
            this.commissionStr = MoneyUtil.fenToYuan(this.getCommission());
        }
    }

    public void setQuoteTimeSpent() {
        if (this.quoteTimeSpent != null) {
            this.timeSpent = DateUtil.formatBetween(this.quoteTimeSpent, BetweenFormatter.Level.SECOND);
            if (this.quoteTimeSpentReal != null) {
                this.timeSpentReal = DateUtil.formatBetween(this.quoteTimeSpentReal, BetweenFormatter.Level.SECOND);
            }
        }
    }

    public void setQuoteTimeSpentReal() {
        if (this.quoteTimeSpent != null) {
            this.timeSpent = DateUtil.formatBetween(this.quoteTimeSpent, BetweenFormatter.Level.SECOND);
            if (this.quoteTimeSpentReal != null) {
                this.timeSpent = DateUtil.formatBetween(this.quoteTimeSpentReal, BetweenFormatter.Level.SECOND);
            }
        }
    }

    public void setPlatformSubsidyPriceStr() {
        if (this.getPlatformSubsidyPrice() != null && this.getPlatformSubsidyPrice() >= 0) {
            this.platformSubsidyPriceStr = MoneyUtil.fenToYuan(this.getPlatformSubsidyPrice());
        }
    }

    public void setPriceInfo() {
        this.setFinalPriceStr();
        this.setCommissionStr();
        this.setPlatformSubsidyPriceStr();
        this.setOriginalPriceStr();
    }

    public void setOperationBtn(Long employeeId) {
        this.setCanCancel(employeeId);
        this.setCanLock();
        this.setCanQuote(employeeId);
    }

    public void setCanCancel(Long employeeId) {
        this.canCancel = false;
        if (employeeId == null) {
            return;
        }
        boolean rightStatus = this.status.equals(OrderQuoteLogStatusEnum.QUOTING.getCode());
        boolean self = employeeId.equals(this.employeeId);
        if (rightStatus && self) {
            this.canCancel = true;
        }
    }

    public void setCanLock() {
        this.canLock = this.status.equals(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode());
    }

    public void setCanQuote(Long employeeId) {
        this.canQuote = false;
        if (employeeId == null) {
            return;
        }
        boolean rightStatus = this.status.equals(OrderQuoteLogStatusEnum.QUOTING.getCode());
        boolean self = employeeId.equals(this.employeeId);
        if (rightStatus && self) {
            this.canQuote = true;
        }
    }
}