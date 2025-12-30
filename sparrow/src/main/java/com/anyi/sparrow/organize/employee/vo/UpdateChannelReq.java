package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateChannelReq{
    @ApiModelProperty("渠道id")
    @NotNull
    private Long channelId;
    @ApiModelProperty("当前仅支持修改所属部门")
    private Long pDeptId;
}
