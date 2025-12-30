package com.anyi.common.insurance.req;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
public class GzhPassWordReq  implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty("旧密码")
    private String oldPass;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String newPass;

    @ApiModelProperty("手机号后端自己传")
    private String mobile;


}