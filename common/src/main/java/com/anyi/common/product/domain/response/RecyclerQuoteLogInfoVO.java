package com.anyi.common.product.domain.response;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.product.domain.enums.OrderQuoteLogStatusEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogSubStatusEnum;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
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
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("回收商报价中心报价单信息响应对象")
public class RecyclerQuoteLogInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报价记录ID")
    private Long id;

    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", notes = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "报价订单ID")
    private Long orderId;
    @ApiModelProperty(value = "回收商员工ID")
    private Long employeeId;
    @ApiModelProperty(value = "回收商员名称")
    private String employeeName;
    @ApiModelProperty(value = "回收商ID")
    private Long companyId;
    @ApiModelProperty(value = "回收商名称")
    private String companyName;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer originalQuotePrice;
    @ApiModelProperty(value = "原始报价")
    private String originalQuotePriceStr;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer finalPrice;
    @ApiModelProperty(value = "成交价")
    private String finalPriceStr;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer actualPaymentPrice;
    @ApiModelProperty(value = "实际付款价格=原始报价+平台抽成金额")
    private String actualPaymentPriceStr;
    @ApiModelProperty(value = "平台抽成规则", hidden = true)
    private BigDecimal platformCommissionRule;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer platformCommission;
    @ApiModelProperty(value = "平台抽成金额")
    private String platformCommissionStr;
    @ApiModelProperty(value = "门店抽成规则", hidden = true)
    private BigDecimal commissionRule;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer commission;
    @ApiModelProperty(value = "门店抽成金额")
    private String commissionStr;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer platformSubsidyPrice;
    @ApiModelProperty("平台补贴")
    private String platformSubsidyPriceStr;
    @ApiModelProperty(value = "是否已报价，默认为否")
    private Boolean quoted;
    @ApiModelProperty(value = "报价时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quoteTime;
    @ApiModelProperty(value = "总体报价用时=报价时间-报价订单创建时间 单位毫秒", hidden = true)
    private Long quoteTimeSpent;
    @ApiModelProperty(value = "实际报价用时=报价时间-抢单时间 单位毫秒 单位毫秒", hidden = true)
    private Long quoteTimeSpentReal;
    @ApiModelProperty(value = "总体报价用时", example = "15分41秒")
    private String timeSpent;
    @ApiModelProperty(value = "实际报价用时", example = "15分41秒")
    private String timeSpentReal;

    /**
     * @see OrderQuoteLogStatusEnum
     */
    @ApiModelProperty(value = "报价状态 0待报价 1报价中 2已报价 -1已作废")
    private Integer status;
    /**
     * @see OrderQuoteLogSubStatusEnum
     */
    @ApiModelProperty(value = "报价子状态 0待报价 1报价中 2已确认交易 3未入选 4超时未报价 5超时未确认交易 6交易取消 7交易退款")
    private Integer subStatus;

    @ApiModelProperty(value = "报价订单信息")
    private OrderDetailVO orderInfo;

    @ApiModelProperty(value = "是否可以取消报价，报价中且自己的报价单才能取消")
    private Boolean canCancel;

    @ApiModelProperty(value = "是否可以抢单")
    private Boolean canLock;

    @ApiModelProperty(value = "是否可以报价")
    private Boolean canQuote;

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

    public void setPriceInfo() {
        this.setOriginalQuotePriceStr();
        this.setFinalPriceStr();
        this.setActualPaymentPriceStr();
        this.setPlatformCommissionStr();
        this.setCommissionStr();
        this.setPlatformSubsidyPriceStr();
    }

    public void setOriginalQuotePriceStr() {
        if (this.originalQuotePrice != null && this.originalQuotePrice >= 0) {
            this.originalQuotePriceStr = MoneyUtil.fenToYuan(this.originalQuotePrice);
        }
    }

    public void setFinalPriceStr() {
        if (this.finalPrice != null && this.finalPrice >= 0) {
            this.finalPriceStr = MoneyUtil.fenToYuan(this.finalPrice);
        }
    }

    public void setActualPaymentPriceStr() {
        if (this.actualPaymentPrice != null && this.actualPaymentPrice >= 0) {
            this.actualPaymentPriceStr = MoneyUtil.fenToYuan(this.actualPaymentPrice);
        }
    }

    public void setPlatformCommissionStr() {
        if (this.platformCommission != null && this.platformCommission >= 0) {
            this.platformCommissionStr = MoneyUtil.fenToYuan(this.platformCommission);
        }
    }

    public void setCommissionStr() {
        if (this.commission != null && this.commission >= 0) {
            this.commissionStr = MoneyUtil.fenToYuan(this.commission);
        }
    }

    public void setPlatformSubsidyPriceStr() {
        if (this.platformSubsidyPrice != null && this.platformSubsidyPrice >= 0) {
            this.platformSubsidyPriceStr = MoneyUtil.fenToYuan(this.platformSubsidyPrice);
        }
    }
}