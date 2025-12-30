package com.anyi.common.mbr.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MbrChangeOrderNotifyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店唯一标识")
    private Long storeEmployeeId;

    @ApiModelProperty("第三方订单号")
    private Long thirdOrderId;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("结算地址")
    private String settleLink;


}