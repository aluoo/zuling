package com.anyi.sparrow.organize.employee.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class EmployeeMobileQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;
}