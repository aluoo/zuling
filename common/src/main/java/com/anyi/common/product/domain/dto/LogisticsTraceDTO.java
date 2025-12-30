package com.anyi.common.product.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/20
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsTraceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "节点名称", example = "揽收任务分配")
    private String operationTitle;
    @ApiModelProperty(value = "节点描述", example = "揽收任务已分配给高凡（联系电话：13100000001...")
    private String operationRemark;
    @ApiModelProperty(value = "操作人姓名", example = "高凡")
    private String operatorName;
    @ApiModelProperty(value = "操作时间，格式：yyyy-MM-dd HH:mm:ss", example = "2022-07-31 15:31:28")
    private String operationTime;
    @ApiModelProperty(value = "运单号", example = "JDX010301626950")
    private String waybillCode;
    @ApiModelProperty(value = "状态编码", example = "200034")
    private String state;
    @ApiModelProperty(value = "环节编码", example = "390")
    private String category;
    @ApiModelProperty(value = "环节描述", example = "揽收中")
    private String categoryName;
}