package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmpCfgVo {
    @ApiModelProperty("名字")
    private String name;
    @ApiModelProperty("编号")
    private String no;
    @ApiModelProperty("1 齐鲁工行 2齐鲁交通 3 江苏交行")
    @NotNull
    private Integer biz;
    @ApiModelProperty("扩展编号 齐鲁工行返回")
    private String pairNo;
}
