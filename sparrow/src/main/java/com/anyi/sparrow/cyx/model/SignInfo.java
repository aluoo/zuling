package com.anyi.sparrow.cyx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lks
 * @version 1.0
 * @description: 第三方请求参数VO
 * @date 2021/12/16
 */
@ToString
@Data
@ApiModel("请求头-签名")
public class SignInfo implements Serializable {
    private static final long serialVersionUID = -1295341401898282061L;

    @ApiModelProperty(value = "公共参数-时间戳")
    private String timestamp;

    @ApiModelProperty(value = "公共参数-签名")
    private String sign;

    @ApiModelProperty(value = "公共参数-防重放码")
    private String nonceStr;

    @ApiModelProperty(value = "公共参数-签名算法类型")
    private String signtype;

    @ApiModelProperty(value = "公共参数-appID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String appid;


    public boolean necessaryParameterVerification() {
        return appid != null;
    }


}
