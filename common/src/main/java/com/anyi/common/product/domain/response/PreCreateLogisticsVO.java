package com.anyi.common.product.domain.response;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.product.domain.dto.AddressDTO;
import com.anyi.common.product.domain.dto.TrackCompanyDTO;
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
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("物流下单发货前置信息响应对象")
public class PreCreateLogisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货订单号")
    private Long shippingOrderId;
    @ApiModelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("发货订单状态 0待下单 1待收货 2已收货 -1已取消")
    private Integer status;
    @ApiModelProperty("物流公司列表")
    private List<TrackCompanyDTO> trackCompanies;
    @ApiModelProperty("发货地址列表")
    private List<AddressDTO> senderAddress;
    @ApiModelProperty("收货地址")
    private AddressDTO receiveAddress;
    @ApiModelProperty("订单信息列表")
    private List<OrderDetailVO> orders;
    @ApiModelProperty("图片列表")
    private List<String> images;

    @ApiModelProperty(value = "订单过期时间", notes = "订单过期自动关闭时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    public void setExpiredTime(int expiredMinutes) {
        DateTime expiredTime = DateUtil.offset(DateUtil.date(this.getCreateTime()), DateField.MINUTE, expiredMinutes);
        this.expiredTime = expiredTime.toJdkDate();
    }
}