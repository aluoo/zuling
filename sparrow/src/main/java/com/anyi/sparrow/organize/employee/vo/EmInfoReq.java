package com.anyi.sparrow.organize.employee.vo;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class EmInfoReq {
    @ApiModelProperty("手机号")
//    @NotEmpty
//    @Length(max = 11, min = 11)
    private String mobileNumber;
    @ApiModelProperty("姓名")
    @NotEmpty
    private String name;
    @NotNull
    @ApiModelProperty("部门id")
    private Long deptId;

    public void setMobileNumber(String mobileNumber) {
        if ((StringUtils.isBlank(mobileNumber) || mobileNumber.length() != 11)){
            throw new BusinessException(BizError.MOBILE_LENGTH_ERROR);
        }
        this.mobileNumber = mobileNumber;
    }
}
