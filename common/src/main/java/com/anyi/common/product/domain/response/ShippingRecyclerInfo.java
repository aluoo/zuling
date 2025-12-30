package com.anyi.common.product.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("发货中心回收商信息响应对象")
public class ShippingRecyclerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回收商ID")
    private Long companyId;
    @ApiModelProperty("回收商名称")
    private String companyName;
    @ApiModelProperty("待发货数量")
    private Integer pendingShipmentCount;
    @ApiModelProperty("超时未发货数量")
    private Integer overdueCount;
    @ApiModelProperty("发货途中数量")
    private Integer pendingReceiptCount;
}