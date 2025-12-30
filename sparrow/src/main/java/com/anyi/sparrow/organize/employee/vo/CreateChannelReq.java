package com.anyi.sparrow.organize.employee.vo;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CreateChannelReq {
    @ApiModelProperty("渠道名称")
    @NotEmpty
    private String name;
    @ApiModelProperty("负责人手机号")
//    @NotEmpty
//    @Length(min = 11, max = 11)
    private String mobile;
    @ApiModelProperty("负责人名字")
    @NotEmpty
    private String managerName;
    @ApiModelProperty("管理部门id")
    private Long pDeptId;

    public void setMobile(String mobile) {
        if ((StringUtils.isBlank(mobile) || mobile.length() != 11)){
            throw new BusinessException(BizError.MOBILE_LENGTH_ERROR);
        }
        this.mobile = mobile;
    }
}
