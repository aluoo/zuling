package com.anyi.sparrow.applet.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author peng can
 * @date 2022/12/4 11:59
 */
@Data
@ApiModel("用户基本信息")
public class UserInfoVO {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户手机号码")
    private String mobileNumber;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("openid")
    private String openId;

    @ApiModelProperty("xyOpenid")
    private String xyOpenId;

    @ApiModelProperty("用户头像")
    private String avatarUrl;

    @ApiModelProperty("unionId")
    private String unionId;

}
