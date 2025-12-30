package com.anyi.sparrow.account.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRealNameVerificationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //private Long id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("身份证正面")
    private String idUrlUp;
    @ApiModelProperty("身份证反面")
    private String idUrlDown;

    @ApiModelProperty("实名认证结果 1一致 2不一致 3不存在")
    private Integer verifyResult;
}