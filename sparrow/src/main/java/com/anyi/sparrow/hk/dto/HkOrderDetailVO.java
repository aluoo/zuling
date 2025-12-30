package com.anyi.sparrow.hk.dto;

import cn.hutool.core.util.EnumUtil;
import com.anyi.sparrow.hk.enums.HkOrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HkOrderDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("商品名称")
    private String fetchName;

    @ApiModelProperty("商品编码")
    private String fetchCode;

    @ApiModelProperty("第三方订单号")
    private String thirdOrderSn;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("省份")
    private String provinceName;

    @ApiModelProperty("市")
    private String cityName;

    @ApiModelProperty("区")
    private String townName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("预约手机号")
    private String planMobileNumber;

    /**
     * @see HkOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("状态中文")
    private String statusName;

    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Long employeeId;
    private Long companyId;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工手机号")
    private String employeeMobile;

    @ApiModelProperty("门店名称")
    private String companyName;

    @ApiModelProperty("理由")
    private String reason;

    @ApiModelProperty("物流单号")
    private String expressBill;

    @ApiModelProperty("物流公司")
    private String express;
    @ApiModelProperty("运营商")
    private String operatorName;
    @ApiModelProperty("供应商")
    private String supplierName;

    public String getStatusName(){
        return Optional.ofNullable(EnumUtil.getBy(HkOrderStatusEnum::getCode, this.status))
                .map(HkOrderStatusEnum::getName)
                .orElse(HkOrderStatusEnum.UNKNOWN.getName());
    }
}