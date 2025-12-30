package com.anyi.sparrow.organize.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateEmReq extends EmInfoReq {
    @ApiModelProperty("员工id")
    @NotNull
    private Long emId;
}
