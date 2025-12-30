package com.anyi.common.product.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/4/7
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("换机晒单列表响应对象")
public class ExchangeOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private Long id;

    @ApiModelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("门店名称")
    private String storeCompanyName;

    @ApiModelProperty("店员ID")
    private Long storeEmployeeId;
    @ApiModelProperty("店员名称")
    private String storeEmployeeName;

    @ApiModelProperty("订单类型3换机4拉新")
    private Integer type;

    @ApiModelProperty("订单类型3换机4拉新5绿洲6苹果抖音")
    private String typeName;

    @ApiModelProperty("来源")
    private Integer source;

    @ApiModelProperty("审核状态")
    private Integer status;

    @ApiModelProperty("结算状态")
    private Integer settleStatus;

    @ApiModelProperty("审核时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date trialTime;

    @ApiModelProperty("图片列表")
    private List<String> images;

    @ApiModelProperty("审核失败原因")
    private String failedReason;

    @ApiModelProperty("机审核状态")
    private Integer sysStatus;

    @ApiModelProperty("审核备注")
    private String sysRemark;

    @ApiModelProperty("平台审核表示")
    private Boolean platCheck;

}