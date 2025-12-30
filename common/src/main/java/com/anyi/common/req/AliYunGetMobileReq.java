package com.anyi.common.req;

import io.swagger.annotations.ApiModel;
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
 * @Date 2024/5/29
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("阿里云一键登录取号请求对象")
public class AliYunGetMobileReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("AccessToken")
    @NotBlank(message = "AccessToken不能为空")
    private String accessToken;
}