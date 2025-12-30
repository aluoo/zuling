package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CompanyVO {
    @ApiModelProperty("渠道id")
    private Long id;

    @ApiModelProperty("渠道名字")
    private String name;

    @ApiModelProperty("渠道负责人")
    private String managerName;

    @ApiModelProperty("手机号")
    private String managerPhone;
    @ApiModelProperty("管理部门名称")
    private String managerDept;
    @ApiModelProperty("管理部门id")
    private Long mangerDeptId;
}
