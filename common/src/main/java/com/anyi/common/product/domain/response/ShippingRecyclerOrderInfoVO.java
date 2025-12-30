package com.anyi.common.product.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发货中心回收商订单信息响应对象")
public class ShippingRecyclerOrderInfoVO extends OrderBaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("已成交天数 当日成交的天数为0")
    private Integer confirmDay;
    @ApiModelProperty("图片列表")
    private List<String> images;
}