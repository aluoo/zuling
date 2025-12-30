package com.anyi.common.product.domain.response;

import cn.hutool.core.util.ArrayUtil;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/29
 * @Copyright
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(value = { "handler", "fieldHandler"})
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("报价订单基础响应对象")
public abstract class OrderBaseVO implements Serializable, ICompanyInfoVO, IEmployeeInfoVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private Long id;
    @ApiModelProperty("订单码")
    private String orderNo;
    @ApiModelProperty("商品ID")
    private Long productId;
    @ApiModelProperty("商品名称")
    private String productName;
    @ApiModelProperty("订单状态")
    private Integer status;
    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("下单人ID(门店员工ID)")
    private Long storeEmployeeId;
    @ApiModelProperty(value = "门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("下单人姓名(门店员工姓名)")
    private String storeEmployeeName;
    @ApiModelProperty("下单人电话")
    private String storeEmployeeMobile;
    @ApiModelProperty("下单门店名称")
    private String storeCompanyName;
    @ApiModelProperty(value = "回收商员工ID")
    private Long recyclerEmployeeId;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;
    @ApiModelProperty(value = "回收商名称")
    private String recyclerCompanyName;
    @ApiModelProperty("回收商报价师姓名")
    private String recyclerEmployeeName;
    @ApiModelProperty("回收商报价师电话")
    private String recyclerEmployeeMobile;
    @ApiModelProperty(value = "IMEI号")
    private String imeiNo;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否绑码", example = "true")
    private Boolean bound;
    @ApiModelProperty(value = "是否核验", example = "true")
    private Boolean verified;
    @ApiModelProperty(value = "是否可报价（超时将关闭报价功能）", example = "true")
    private Boolean quotable;
    @ApiModelProperty(value = "原始报价", hidden = true)
    @JsonIgnore
    private Integer originalQuotePrice;
    @ApiModelProperty(value = "成交价", hidden = true)
    @JsonIgnore
    private Integer finalPrice;
    @ApiModelProperty(value = "成交价格 (最终确认的报价，用户实际收款的价格，用户退款的价格，回收商报价的价格)")
    private String finalPriceStr;
    @ApiModelProperty(value = "门店抽成金额", hidden = true)
    @JsonIgnore
    private Integer commission;
    @ApiModelProperty(value = "门店压价")
    private String commissionStr;
    @ApiModelProperty(value = "平台补贴价格", hidden = true)
    @JsonIgnore
    private Integer platformSubsidyPrice;
    @ApiModelProperty(value = "平台补贴价格")
    private String platformSubsidyPriceStr;
    @ApiModelProperty(value = "确认报价详情ID", hidden = true)
    @JsonIgnore
    private Long quotePriceLogId;
    @ApiModelProperty("确认报价时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishQuoteTime;

    @ApiModelProperty(value = "商品规格", example = "16G+512G")
    private String spec;
    @ApiModelProperty("品牌logo")
    private String brandLogo;

    @ApiModelProperty("已报价的回收商数量 没有则为0")
    private Integer quotedRecyclerCount;

    @ApiModelProperty(value = "是否可以取消")
    private Boolean canCancel;

    @ApiModelProperty(value = "是否可以确认交易")
    private Boolean canConfirmQuote;

    @ApiModelProperty(value = "是否可以收款")
    private Boolean canReceivePayment;

    @ApiModelProperty(value = "是否可申请退款")
    private Boolean canApplyRefund;

    @ApiModelProperty(value = "是否可重新询价")
    private Boolean canCopyOrderBtn;

    @ApiModelProperty(value = "是否可绑码")
    private Boolean canBindBtn;
    @ApiModelProperty(value = "是否可核验")
    private Boolean canVerifyBtn;
    @ApiModelProperty(value = "是否可发货")
    private Boolean canShippingBtn;
    @ApiModelProperty(value = "是否可申请退款")
    private Boolean canApplyRefundBtn;

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

    public void setPlatformSubsidyPriceStr() {
        if (this.getPlatformSubsidyPrice() != null && this.getPlatformSubsidyPrice() >= 0) {
            this.platformSubsidyPriceStr = MoneyUtil.fenToYuan(this.getPlatformSubsidyPrice());
        }
    }

    public void setPriceInfo() {
        this.setFinalPriceStr();
        this.setCommissionStr();
        this.setPlatformSubsidyPriceStr();
    }

    public static <T> Set<T> extractIds(Collection<? extends OrderBaseVO> vos, Function<? super OrderBaseVO, T> ext1, Function<? super OrderBaseVO, T> ext2) {
        return vos.stream()
                .flatMap(o -> Stream.concat(
                        Optional.ofNullable(ext1.apply(o)).map(Stream::of).orElseGet(Stream::empty),
                        Optional.ofNullable(ext2.apply(o)).map(Stream::of).orElseGet(Stream::empty)
                ))
                .collect(Collectors.toSet());
    }

    public void setOperationBtn(Long employeeId, Long companyId) {
        this.setCanCancel(employeeId, companyId);
        this.setCanConfirmQuote(employeeId, companyId);
        this.setCanReceivePayment(employeeId, companyId);
        this.setCanCopyOrderBtn(employeeId, companyId);
        this.setCanApplyRefundBtn(employeeId, companyId);
        this.setCanBindBtn(employeeId, companyId);
        this.setCanVerifyBtn(employeeId, companyId);
        this.setCanShippingBtn(employeeId, companyId);
    }

    public void setCanCancel(Long employeeId, Long companyId) {
        this.canCancel = false;
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (this.checkStatus(OrderStatusEnum.UNCHECKED.getCode(), OrderStatusEnum.PENDING_PAYMENT.getCode()) && self && selfCompany) {
            this.canCancel = true;
        }
    }

    public void setCanConfirmQuote(Long employeeId, Long companyId) {
        this.canConfirmQuote = false;
        if (employeeId == null) {
            return;
        }
        boolean rightStatus = this.checkStatus(OrderStatusEnum.UNCHECKED.getCode());
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean hasQuote = this.getQuotedRecyclerCount() != null && this.getQuotedRecyclerCount() > 0;
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());

        if (rightStatus && self && hasQuote && selfCompany) {
            this.canConfirmQuote = true;
        }
    }

    public void setCanReceivePayment(Long employeeId, Long companyId) {
        this.canReceivePayment = false;
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (this.checkStatus(OrderStatusEnum.PENDING_PAYMENT.getCode()) && self && selfCompany) {
            this.canReceivePayment = true;
        }
    }

    public void setCanCopyOrderBtn(Long employeeId, Long companyId) {
        this.canCopyOrderBtn = false;
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (!this.checkStatus(OrderStatusEnum.FINISHED.getCode()) && self && selfCompany) {
            this.canCopyOrderBtn = true;
        }
    }

    public void setCanApplyRefundBtn(Long employeeId, Long companyId) {
        if (!this.canApplyRefund) {
            return;
        }
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (this.checkStatus(OrderStatusEnum.PENDING_SHIPMENT.getCode()) && self && selfCompany) {
            this.canApplyRefundBtn = true;
        }
    }

    public void setCanBindBtn(Long employeeId, Long companyId) {
        this.canBindBtn = false;
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (this.checkStatus(OrderStatusEnum.PENDING_SHIPMENT.getCode()) && self && selfCompany) {
            this.canBindBtn = true;
        }
    }

    public void setCanVerifyBtn(Long employeeId, Long companyId) {
        this.canVerifyBtn = false;
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (this.checkStatus(OrderStatusEnum.PENDING_SHIPMENT.getCode()) && self && selfCompany) {
            this.canVerifyBtn = true;
        }
    }

    public void setCanShippingBtn(Long employeeId, Long companyId) {
        this.canShippingBtn = false;
        if (employeeId == null) {
            return;
        }
        boolean self = this.getStoreEmployeeId() != null && employeeId.equals(this.getStoreEmployeeId());
        boolean selfCompany = this.getStoreCompanyId() != null && companyId.equals(this.getStoreCompanyId());
        if (this.checkStatus(OrderStatusEnum.PENDING_SHIPMENT.getCode()) && self && selfCompany) {
            this.canShippingBtn = true;
        }
    }

    protected boolean checkStatus(Integer... statusList) {
        return ArrayUtil.contains(statusList, this.status);
    }
}