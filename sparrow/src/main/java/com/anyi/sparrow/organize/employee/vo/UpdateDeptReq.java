package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateDeptReq extends CreateDeptReq {
    @ApiModelProperty("部门id")
    @NotNull
    private Long deptId;
}
