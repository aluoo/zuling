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
 * @Date 2024/3/7
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("发货中心回收商订单核验数量信息响应对象")
public class RecyclerOrderVerifyCountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("未核验数量")
    private Integer unverifiedCount;
    @ApiModelProperty("已核验数量")
    private Integer verifiedCount;
}