package com.anyi.common.product.domain.request;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("报价订单查询请求对象")
public class OrderQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开始时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("订单号")
    private Long orderId;
    @ApiModelProperty("订单码")
    private String orderNo;
    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("订单搜索关键词，输入订单号或订单码查询")
    private String keyword;

    @ApiModelProperty(value = "只查看自己，不传默认查看全部", example = "false")
    @Builder.Default
    private Boolean onlySelf = false;
}