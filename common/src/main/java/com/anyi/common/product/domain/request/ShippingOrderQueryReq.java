package com.anyi.common.product.domain.request;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("物流订单信息查询请求对象")
public class ShippingOrderQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货订单ID")
    private Long shippingOrderId;
    @ApiModelProperty("快递物流单号")
    private String trackNo;
    @ApiModelProperty("发货订单状态 0待下单 1待收货 2已收货 -1已取消")
    private Integer status;
}