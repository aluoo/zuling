package com.anyi.common.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RtaReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("androidid")
    private String aid;
    @ApiModelProperty("唯一设备标识符 android广告标识符")
    private String oaid;
    @ApiModelProperty("IMEI")
    private String imei;
    @ApiModelProperty("ip")
    private String ip;
    @ApiModelProperty("model")
    private String model;
    @ApiModelProperty("后端自己传")
    private String channel;
    @ApiModelProperty("后端自己传")
    private String token;

}
