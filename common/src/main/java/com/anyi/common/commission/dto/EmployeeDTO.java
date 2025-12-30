package com.anyi.common.commission.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    @ApiModelProperty("员工Id")
    private Long memberId;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("人员层级")
    private Integer level;

    @ApiModelProperty("部门Flag")
    private boolean deptFlag;
    @ApiModelProperty("渠道Flag")
    private boolean channelFlag;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("人员组织层级编码")
    private String ancestors;

    @ApiModelProperty("是否已分配方案")
    private boolean hasPlan;

    @ApiModelProperty("方案Id")
    private Long planId;

    @ApiModelProperty("方案名称")
    private String planName;


}
