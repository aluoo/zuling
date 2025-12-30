package com.anyi.common.product.domain.request;

import com.anyi.common.product.domain.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("物流下单请求对象")
public class CreateLogisticsReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货订单号")
    @NotNull(message = "订单号不能为空")
    private Long shippingOrderId;

    @ApiModelProperty("寄件类型 1线上 2线下")
    @NotNull(message = "寄件类型不能为空")
    private Integer shippingType;

    @ApiModelProperty("图片列表")
    private List<String> images;

    @ApiModelProperty("发货地址信息")
    @Valid
    @NotNull(message = "发货地址不能为空")
    private AddressDTO sendAddress;
    @ApiModelProperty("收货地址信息")
    @Valid
    @NotNull(message = "收货地址不能为空")
    private AddressDTO receiveAddress;

    @ApiModelProperty("快递公司编码")
    @NotBlank(message = "快递公司不能为空")
    private String trackCompanyCode;
    @ApiModelProperty("快递公司名称")
    @NotBlank(message = "快递公司不能为空")
    private String trackCompanyName;
    @ApiModelProperty("快递物流单号")
    private String trackNo;

    @ApiModelProperty("期望揽收开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pickupStartTime;
    @ApiModelProperty("期望揽收结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pickupEndTime;
}