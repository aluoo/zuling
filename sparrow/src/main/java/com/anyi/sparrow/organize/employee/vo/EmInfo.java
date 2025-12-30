package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmInfo {
    @ApiModelProperty("员工id")
    private Long id;
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("员工姓名")
    private String name;
    @ApiModelProperty("电话")
    private String mobileNumber;
    @ApiModelProperty("公司id")
    private Long companyId;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("是否是物料中心")
    private boolean matCenter = false;
}
