package com.anyi.common.account.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class QueryAccountReq {
    @ApiModelProperty(value = "看具体某个人明细时传，看自己的不用传")
    private Long employeeId;
}
