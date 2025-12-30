package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DeptVO {
    @ApiModelProperty("部门id")
    private Long id;

    @ApiModelProperty("部门名字")
    private String name;

    @ApiModelProperty("所属部门名字")
    private Long pdeptId;

    @ApiModelProperty("所属部门名字")
    private String pdeptName;

    @ApiModelProperty("部门管理员")
    private String managerName;

    @ApiModelProperty("手机号")
    private String managerPhone;
}
