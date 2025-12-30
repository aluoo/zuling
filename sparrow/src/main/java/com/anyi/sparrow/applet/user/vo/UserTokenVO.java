package com.anyi.sparrow.applet.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author peng can
 * @date 2022/12/4 10:42
 */
@Data
@ApiModel("登陆返回对象")
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenVO {

    @ApiModelProperty("用户token")
    private String token;

    @ApiModelProperty("过期时间 (秒)")
    private Long expireTime;
}
