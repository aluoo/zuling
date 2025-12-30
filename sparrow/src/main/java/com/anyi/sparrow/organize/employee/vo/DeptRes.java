package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptRes {
    @ApiModelProperty("是否并入老部门 是为true")
    private boolean addToOldDept = false;
    @ApiModelProperty("提醒信息")
    private String remind;
}