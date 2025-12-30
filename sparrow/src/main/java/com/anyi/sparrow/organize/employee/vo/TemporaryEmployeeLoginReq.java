package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporaryEmployeeLoginReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    @NotBlank
    private String mobile;
    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    @ApiModelProperty("设备信息")
    private String device;

    @ApiModelProperty("系统 （android, ios）")
    private String os;

    @ApiModelProperty("系统版本")
    private String osVersion;

    @ApiModelProperty("app版本")
    private String appVersion;
}