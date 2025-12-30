package com.anyi.common.mbr.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MbrHwzCreateOrderNotifyReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String method;
    private OrderDetail data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderDetail implements Serializable {
        @ApiModelProperty("订单id")
        private Long saleOrderId;

        @ApiModelProperty("订单状态")
        private Integer status;

        @ApiModelProperty("外部店铺id/业务员id，需要唯一")
        private String outShopId;

        @ApiModelProperty("手机序列号")
        private String serialNumber;

        @ApiModelProperty("创建时间")
        private String createTime;

        @ApiModelProperty("商品名称")
        private String skuName;

        @ApiModelProperty("商品规格")
        private List<SkuSpec> spec;

        @ApiModelProperty("用户姓名")
        private String memberName;
        @ApiModelProperty("用户手机号")
        private String memberMobile;
        @ApiModelProperty("用户身份找")
        private String idCard;

        @ApiModelProperty("荟玩租店铺Id")
        private Long shopId;

        @ApiModelProperty("设备详情")
        private Device device;

        @ApiModelProperty("还款详情")
        private List<SaleOrderPhase> phases;

        @ApiModelProperty("期数")
        private Integer rentPhase;

        @ApiModelProperty("新机二手")
        private String secondHand;

        @ApiModelProperty("总租金（方案金额）")
        private BigDecimal totalRent;

        @ApiModelProperty("商品成本")
        private BigDecimal productCost;

        @ApiModelProperty("押金")
        private BigDecimal deposit;

        @ApiModelProperty("通过门店雇员码下单时存在")
        private String outEmployeeId;

        @ApiModelProperty("结算地址")
        private String settleLink;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SkuSpec implements Serializable {
        private static final long serialVersionUID = 1L;
        private String specName;
        private String specValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Device implements Serializable {
        private static final long serialVersionUID = 1L;
        private String serialNumber;
        private String lastConnectTime;
        private String control;
        private String lostStatus;
        private String productName;
        private String imei;
        private String meid;
        private String battery;
        private Boolean activationLockEnabled;
        private String phoneNumber;
        private String osVersion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SaleOrderPhase implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer number;

        private String startDate;
        private String endDate;
        private String expectPayDate;
        private String completedAt;

        private BigDecimal totalRent;
        private BigDecimal paidRent;
        private Boolean completed;
        private Integer overdueDay;
        private BigDecimal overdueInterest;
        private BigDecimal paidOverdueInterest;
    }


}