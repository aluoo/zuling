package com.anyi.sparrow.cyx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 接口请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/11/14
 */
@Data
@ApiModel
public class QueryOrderReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号")
    private String orderNo;
}