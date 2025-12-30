package com.anyi.common.exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class DeviceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一设备标识符 android广告标识符")
    private String oaid;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("系统版本号")
    private String sysVersion;

    @ApiModelProperty("安装日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date installDate;

    @ApiModelProperty("型号")
    private String brand;

    @ApiModelProperty("安卓版本号")
    private String androidVersion;

    private Boolean exchangeMode;

}