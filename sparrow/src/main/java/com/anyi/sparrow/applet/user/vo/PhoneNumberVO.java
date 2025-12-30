package com.anyi.sparrow.applet.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author peng can
 * @date 2022/12/3 18:39
 */
@ApiModel("用户手机号码")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberVO {

    @ApiModelProperty("手机号码")
    private String mobileNumber;
}
