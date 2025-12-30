package com.anyi.sparrow.organize.employee.vo;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CreateDeptReq {
    @ApiModelProperty("部门名称")
    @NotEmpty
    private String name;
    @ApiModelProperty("管理员名称")
    private String managerName;
    @ApiModelProperty("管理员手机号")
    private String mobile;
    @ApiModelProperty("父部门id")
    private Long pdeptId;

    public void setMobile(String mobile) {
        if ((StringUtils.isNotBlank(mobile) && mobile.length() != 11)){
            throw new BusinessException(BizError.MOBILE_LENGTH_ERROR);
        }
        this.mobile = mobile;
    }
}
