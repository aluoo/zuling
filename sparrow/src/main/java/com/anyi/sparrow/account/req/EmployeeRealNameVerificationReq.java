package com.anyi.sparrow.account.req;

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
 * @Date 2023/10/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRealNameVerificationReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;
    @ApiModelProperty("身份证正面")
    @NotBlank(message = "身份证图片不能为空")
    private String idUrlUp;
    @ApiModelProperty("身份证反面")
    @NotBlank(message = "身份证图片不能为空")
    private String idUrlDown;
}