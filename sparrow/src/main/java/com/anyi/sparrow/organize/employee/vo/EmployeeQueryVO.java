package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("1渠道 2部门 3人员")
    private Integer type;
    // @ApiModelProperty("团队人数")
    // private Integer total;
    @ApiModelProperty("人员ID")
    private Long id;
    @ApiModelProperty("部门ID")
    private Long deptId;
    @ApiModelProperty("个人名字")
    private String name;
    @ApiModelProperty("电话")
    private String mobileNumber;
    @ApiModelProperty("渠道/部门名字")
    private String deptName;
    @ApiModelProperty("是否管理员")
    private Boolean isManager;
    @ApiModelProperty("自己能否更新员工信息")
    private Boolean isUpdate;
    @ApiModelProperty("是否是自己")
    private Boolean isSelf;
}