package com.anyi.common.account.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 资金明细列表接口请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@Data
@ApiModel
public class CancelEmployeeReq {

    @NotNull(message = "员工ID不能为空")
    private Long employeeId;


}
