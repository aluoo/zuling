package com.anyi.common.product.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("门店个人中心首页响应对象")
public class StoreCenterIndexVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户信息")
    private UserInfo userInfo;
    @ApiModelProperty("今日收入信息")
    private TodayIncomeInfo billInfo;
    @ApiModelProperty("手机订单中心信息")
    private OrderInfo orderInfo;
    @ApiModelProperty("手机发货中心信息")
    private ShippingOrderInfo shippingOrderInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("门店个人中心-用户数据响应对象")
    public static class UserInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("员工ID")
        private Long employeeId;
        @ApiModelProperty("员工名称")
        private String employeeName;
        @ApiModelProperty("员工手机")
        private String employeeMobile;
        @ApiModelProperty("门店ID")
        private Long companyId;
        @ApiModelProperty("门店名称")
        private String companyName;
        @ApiModelProperty("员工类型")
        private Integer employeeType;
        @ApiModelProperty("门店类型")
        private Integer companyType;
        @ApiModelProperty("是否实名")
        private Boolean realNameVerified;
        @ApiModelProperty("是否店长")
        private Boolean manage;
        @ApiModelProperty("钱包余额")
        private String balance;
        @ApiModelProperty("层级")
        private Integer level;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("门店个人中心-今日收入数据响应对象")
    public static class TodayIncomeInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("日期 yyyy年-MM月-dd日")
        private String today;
        @ApiModelProperty("总收入")
        private String totalIncome;
        @ApiModelProperty("手机回收")
        private String mobileOrderIncome;
        @ApiModelProperty("App拉新")
        private String appNewOrderIncome;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("门店个人中心-手机订单中心数据响应对象")
    public static class OrderInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("待确认")
        private Integer unchecked;
        @ApiModelProperty("待收款")
        private Integer pendingPayment;
        @ApiModelProperty("待发货")
        private Integer pendingShipment;
        @ApiModelProperty("追差/售后")
        private Integer afterSale;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("门店个人中心-手机发货中心数据响应对象")
    public static class ShippingOrderInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("待发货")
        private Integer pendingShipment;
        @ApiModelProperty("待收货")
        private Integer pendingReceipt;
    }
}