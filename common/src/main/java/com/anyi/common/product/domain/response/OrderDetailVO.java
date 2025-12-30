package com.anyi.common.product.domain.response;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.product.domain.dto.OrderQuoteInfoDTO;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/28
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("报价订单详情响应对象")
public class OrderDetailVO extends OrderBaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单过期时间", notes = "订单过期自动关闭时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;
    @ApiModelProperty(value = "订单报价过期时间", notes = "超过该时间关闭报价功能")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quoteExpiredTime;
    @ApiModelProperty("已报价的回收商数量 没有则为0")
    private Integer quotedRecyclerCount;
    @ApiModelProperty("图片列表")
    private List<String> images;

    @ApiModelProperty("规格信息-存储空间")
    private String rom;
    @ApiModelProperty("规格信息-运行内存")
    private String ram;
    @ApiModelProperty("规格信息-颜色")
    private String color;
    @ApiModelProperty("图片信息-主图列表")
    private List<String> masterImages;
    @ApiModelProperty("图片信息-其它图片列表")
    private List<String> otherImages;
    @ApiModelProperty("手机功能-验机报告")
    private List<Report> reports;

    @ApiModelProperty(value = "发货物流信息")
    private ExpressInfo expressInfo;

    @ApiModelProperty(value = "确认交易的回收商信息 确认交易后才有")
    private OrderQuoteInfoDTO recyclerInfo;

    @ApiModelProperty("回收商报价信息列表")
    private List<OrderQuoteInfoDTO> recyclerQuoteInfoList;

    @ApiModelProperty(value = "收款信息")
    private ReceivePaymentInfo paymentInfo;
    @ApiModelProperty(value = "退款信息")
    private RefundPaymentInfo refundInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("报价订单详情-验机报告响应对象")
    public static class Report implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("功能标题")
        private String title;
        @ApiModelProperty("是否正常")
        private Boolean isOk;
        @ApiModelProperty("异常信息")
        private String errorMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("报价订单详情-发货物流信息响应对象")
    public static class ExpressInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("发货订单ID")
        private Long shippingOrderId;
        @ApiModelProperty("快递公司")
        private String trackCompanyName = "京东";
        @ApiModelProperty("快递单号")
        private String trackNo;

        @ApiModelProperty("发货订单创建时间")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createTime;
        @ApiModelProperty("物流下单时间")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date applyLogisticsTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("报价订单详情-收款信息响应对象")
    public static class ReceivePaymentInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("收款人姓名")
        private String name;
        @ApiModelProperty("收款人手机")
        private String mobile;
        @ApiModelProperty("收款人身份证")
        private String idCard;
        @ApiModelProperty(value = "收款金额", hidden = true)
        @JsonIgnore
        private Integer amount;
        @ApiModelProperty("收款金额")
        private String amountStr;
        @ApiModelProperty("收款状态 0待收款 1已收款")
        private Integer receiveStatus;
        @ApiModelProperty("收款时间")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date receiveTime;
        @ApiModelProperty(value = "跳转连接二维码图片地址")
        private String qrCodeUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("报价订单详情-退款信息响应对象")
    public static class RefundPaymentInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(hidden = true)
        @JsonIgnore
        private Integer amount;
        @ApiModelProperty("金额")
        private String amountStr;
        @ApiModelProperty("支付状态 0待支付 1已支付")
        private Integer refundStatus;
        @ApiModelProperty("支付时间")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date payTime;
        @ApiModelProperty(value = "跳转连接二维码图片地址")
        private String qrCodeUrl;
        @ApiModelProperty("退款原因")
        private String reason;
    }

    public void setExpiredTime(int productOrderExpiredMinutes, int quoteExpiredMinutes) {
        if (this.getStatus() != null && this.getStatus().equals(OrderStatusEnum.UNCHECKED.getCode())) {
            this.setExpiredTime(productOrderExpiredMinutes);
            this.setQuoteExpiredTime(quoteExpiredMinutes);
        }
    }

    public void setExpiredTime(int expiredMinutes) {
        DateTime expiredTime = DateUtil.offset(DateUtil.date(super.getCreateTime()), DateField.MINUTE, expiredMinutes);
        this.expiredTime = expiredTime.toJdkDate();
    }

    public void setQuoteExpiredTime(int quoteExpiredMinutes) {
        DateTime expiredTime = DateUtil.offset(DateUtil.date(super.getCreateTime()), DateField.MINUTE, quoteExpiredMinutes);
        this.quoteExpiredTime = expiredTime.toJdkDate();
    }
}