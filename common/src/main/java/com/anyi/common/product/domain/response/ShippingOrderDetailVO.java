package com.anyi.common.product.domain.response;

import com.anyi.common.product.domain.dto.AddressDTO;
import com.anyi.common.product.domain.dto.LogisticsTraceDTO;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@ApiModel("发货订单信息响应对象")
public class ShippingOrderDetailVO implements Serializable, ICompanyInfoVO, IEmployeeInfoVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货订单号")
    private Long id;

    @ApiModelProperty("发货订单状态 0待下单 1待收货 2已收货 -1已取消")
    private Integer status;
    @ApiModelProperty(value = "门店发货员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty(value = "门店发货员工名称")
    private String storeEmployeeName;
    @ApiModelProperty(value = "门店发货员工电话")
    private String storeEmployeeMobile;
    @ApiModelProperty(value = "门店ID")
    private Long storeCompanyId;
    @ApiModelProperty(value = "门店名称")
    private String storeCompanyName;
    @ApiModelProperty(value = "回收商确认收货员工ID")
    private Long recyclerEmployeeId;
    @ApiModelProperty(value = "回收商确认收货员工名称")
    private String recyclerEmployeeName;
    @ApiModelProperty(value = "回收商确认收货员工电话")
    private String recyclerEmployeeMobile;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;
    @ApiModelProperty(value = "回收商名称")
    private String recyclerCompanyName;

    @ApiModelProperty("寄件类型 1线上 2线下")
    private Integer shippingType;

    @ApiModelProperty(value = "快递公司编码", hidden = true)
    @JsonIgnore
    private String trackCompanyCode;
    @ApiModelProperty("快递公司名称")
    private String trackCompanyName;
    @ApiModelProperty("快递物流单号")
    private String trackNo;
    @ApiModelProperty("发货地址")
    private AddressDTO senderAddress;
    @ApiModelProperty("收货地址")
    private AddressDTO receiveAddress;
    @ApiModelProperty("订单信息列表")
    private List<OrderDetailVO> orders;
    @ApiModelProperty("寄出设备数量")
    private Integer orderCount;
    @ApiModelProperty("图片列表")
    private List<String> images;

    @ApiModelProperty("发货订单创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "物流下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyLogisticsTime;
    @ApiModelProperty(value = "确认收货时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmReceiptTime;
    @ApiModelProperty(value = "订单过期时间", notes = "订单过期自动关闭时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    @ApiModelProperty(value = "物流轨迹信息")
    private List<LogisticsTraceDTO> logisticsTrace;

    @ApiModelProperty(value = "运费", hidden = true)
    private Integer price;
    @ApiModelProperty(value = "运费")
    private String priceStr;

    public void setPriceStr() {
        if (this.price != null && this.price >= 0) {
            this.priceStr = MoneyUtil.fenToYuan(this.price);
        }
    }

    public static <T> Set<T> extractIds(List<ShippingOrderDetailVO> vos, Function<ShippingOrderDetailVO, T> ext1, Function<ShippingOrderDetailVO, T> ext2) {
        return vos.stream()
                .flatMap(o -> Stream.concat(
                        Optional.ofNullable(ext1.apply(o)).map(Stream::of).orElseGet(Stream::empty),
                        Optional.ofNullable(ext2.apply(o)).map(Stream::of).orElseGet(Stream::empty)
                ))
                .collect(Collectors.toSet());
    }
}