package com.anyi.common.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeCustomDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("订单号")
    private String orderSn;

    @ApiModelProperty("换机包ID")
    private Long exchangePhoneId;

    @ApiModelProperty("出厂日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date factoryDate;

    @ApiModelProperty("开机时长")
    private Long openTime;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("系统版本号")
    private String sysVersion;

    @ApiModelProperty("后端自己传")
    private Long employeeId;

    @ApiModelProperty("安装包数组")
    private List<InstallDTO> installList;

    @ApiModelProperty("ocpx 请求参数")
    private OcpxReq ocpxReq;

    @ApiModelProperty("应用市场标识1打开0不打开")
    private Integer marketFlag;

    @Data
    public static class InstallDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("安装包ID")
        private Long installId;

        @ApiModelProperty("打开时长秒")
        private Integer openTime;



    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OcpxReq implements Serializable {
        private static final long serialVersionUID = 1L;

        private String os;
        private String aid;
        private String oaid;
        private String imei;
        private String imei1;
        private String idfa;
        private String ip;
        private String ua;
    }
}