package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

@ApiModel
public class EmployeeLoginVO {
    @NotEmpty
    @ApiModelProperty("手机号")
    private String mobile;

    @NotEmpty
    @ApiModelProperty("验证码")
    private String verifyCode;

    @ApiModelProperty("设备信息")
    private String device;

    @ApiModelProperty("系统 （android, ios）")
    private String os;

    @ApiModelProperty("系统版本")
    private String osVersion;

    @ApiModelProperty("app版本")
    private String appVersion;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
