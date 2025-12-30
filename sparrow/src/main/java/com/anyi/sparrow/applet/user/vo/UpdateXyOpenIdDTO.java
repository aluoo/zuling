package com.anyi.sparrow.applet.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shenbh
 * @date 2023/04/24 16:52
 */
@Data
@ApiModel("更新用户xyOpenId")
public class UpdateXyOpenIdDTO {

    @ApiModelProperty("信元openID")
    private String xyOpenId;


}
