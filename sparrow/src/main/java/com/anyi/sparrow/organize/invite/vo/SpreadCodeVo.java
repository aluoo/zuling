package com.anyi.sparrow.organize.invite.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpreadCodeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分享二维码地址")
    private String codeUrl;

    @ApiModelProperty("分享二维码地址")
    private String qrCodeUrl;

    @ApiModelProperty("门店名称")
    private String companyName;


}