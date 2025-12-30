package com.anyi.common.product.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("报价订单提交响应对象")
public class OrderApplyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("回收商列表")
    private List<RecyclerInfo> recyclers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecyclerInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("回收商ID")
        private Long id;

        @ApiModelProperty("回收商头像")
        private String avatar;

        @ApiModelProperty("回收商名称")
        private String name;

    }
}