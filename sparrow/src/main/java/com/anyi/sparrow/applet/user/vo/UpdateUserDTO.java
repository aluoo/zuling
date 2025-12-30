package com.anyi.sparrow.applet.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author peng can
 * @date 2022/12/4 12:42
 */
@Data
@ApiModel("更新用户信息")
public class UpdateUserDTO {

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatarUrl;
}
