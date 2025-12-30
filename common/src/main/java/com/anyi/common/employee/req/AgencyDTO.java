package com.anyi.common.employee.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("人员层级编码")
    @NotBlank(message = "人员层级编码不能为空")
    private String ancestors;
    @ApiModelProperty("部门管理员Level")
    @NotNull(message = "Level不能为空")
    private Integer level;

}
