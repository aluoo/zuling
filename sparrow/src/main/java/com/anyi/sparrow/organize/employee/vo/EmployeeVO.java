package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EmployeeVO {
    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("员工id")
    private Long id;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("公司id")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("公司类型 1安逸出行 2 渠道")
    private Integer companyType;

    @ApiModelProperty("1-管理部门管理员 2-管理部门员工 3-普通部门管理员 4-普通部门员工")
    private Integer type;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("状态 （1 正常 2 已注销）")
    private Byte status;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("手机号")
    private String mobileNumber;

    @ApiModelProperty("头像")
    private String headUrl;

    @ApiModelProperty("部门code")
    private String deptCode;

    @ApiModelProperty("能否更新部门")
    private Boolean isUpdateDept;

    @ApiModelProperty("有用户订单操作权限")
    private Boolean hasUserOrderPerm;

    @ApiModelProperty("人员组织层级编码")
    private String ancestors;

    @ApiModelProperty("人员层级")
    private Integer level;

    @ApiModelProperty("是否能查看团队收益: true- 能(且能设置对公账户) false-不能")
    private boolean teamBenefitsWatchAble = false;

    @ApiModelProperty("是否能查看佣金管理: true- 能 false-不能")
    private boolean commissionPlanWatchAble = false;

    @ApiModelProperty("是否为游客账号: true- 是 false-否")
    private boolean temporaryUser = false;

    @ApiModelProperty("是否为押金系统: 1- 是 0-否")
    private Boolean depositAble;

    // 审核用，默认为false，需要隐藏才为true
    private Boolean reStatus = false;
}