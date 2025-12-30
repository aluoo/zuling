package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryChildRes {
    @ApiModelProperty("渠道/部门 id")
    private Long id;
    @ApiModelProperty("1渠道 2部门")
    private Integer type;
    @ApiModelProperty("总人数")
    private Integer total;
    @ApiModelProperty("负责人/管理员姓名")
    private String manager;
    @ApiModelProperty("渠道/部门名称")
    private String name;
    @ApiModelProperty("自己是否是该部门的管理员")
    private Boolean isManager;
    @ApiModelProperty("自己能否更新该渠道/部门信息")
    private Boolean isUpdate;
    @ApiModelProperty("是否是自己")
    private Boolean isSelf;
    @ApiModelProperty("子信息列表")
    private List<ChildInfo> childInfos;

    @Data
    public static final class ChildInfo {
        @ApiModelProperty("1渠道 2部门 3人员")
        private Integer type;
        @ApiModelProperty("渠道/部门/个人名字")
        private String name;
        @ApiModelProperty("团队人数")
        private Integer total;
        @ApiModelProperty("渠道/部门/人员ID")
        private Long id;
        @ApiModelProperty("是否管理员")
        private Boolean isManager;
        @ApiModelProperty("自己能否更新员工信息")
        private Boolean isUpdate;
        @ApiModelProperty("是否是自己")
        private Boolean isSelf;
    }
}
