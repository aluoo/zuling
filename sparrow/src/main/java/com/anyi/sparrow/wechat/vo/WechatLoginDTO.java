package com.anyi.sparrow.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author peng can
 * @date 2022/12/3 22:14
 */
@Data
@ApiModel("微信登陆请求")
public class WechatLoginDTO {

    @ApiModelProperty("授权code")
    @NotBlank(message = "授权code不能为空")
    private String authCode;

}
