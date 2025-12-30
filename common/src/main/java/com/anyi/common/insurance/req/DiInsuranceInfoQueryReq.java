package com.anyi.common.insurance.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("手机规格-保险产品信息查询对象")
public class DiInsuranceInfoQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机产品ID")
    @NotNull(message = "手机产品ID不能为空")
    private Long productId;
    @ApiModelProperty("手机规格ID")
    @NotNull(message = "手机规格ID不能为空")
    private Long skuId;
}