package com.anyi.common.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("代理总人数")
    private Integer employeeNum;
    @ApiModelProperty("代理团队")
    private List<AgencyChildVO> agencyList;

    @ApiModelProperty("直营门店数目")
    private Integer companyNum;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AgencyChildVO implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("员工ID")
        private Long employeeId;
        @ApiModelProperty("员工名称")
        private String employeeName;
        @ApiModelProperty("员工状态")
        private Integer status;
        @ApiModelProperty("员工状态中文")
        private String statusName;
        @ApiModelProperty("部门名称")
        private String deptName;
        @ApiModelProperty("员工手机号")
        private String phone;
        @ApiModelProperty("成员数量")
        private Integer staffNum;
        @ApiModelProperty("门店数量")
        private Integer companyNum;
        @ApiModelProperty("部门管理员层级")
        private String ancestors;
        @ApiModelProperty("部门管理员Level")
        private int level;
        @ApiModelProperty("下一步是代理")
        private Boolean nextAgency;
        @ApiModelProperty("下一步是门店")
        private Boolean nextCompany;
    }

}
