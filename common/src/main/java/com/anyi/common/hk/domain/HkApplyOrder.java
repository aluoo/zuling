package com.anyi.common.hk.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hk_apply_order")
@ApiModel(value = "号卡订单表")
@Data
public class HkApplyOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("商品编码")
    private String fetchCode;
    @ApiModelProperty("商品名称")
    private String fetchName;
    @ApiModelProperty("三方订单号")
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
    @ApiModelProperty("员工ID")
    private Long employeeId;
    @ApiModelProperty("门店ID")
    private Long companyId;
    @ApiModelProperty("层级")
    private String ancestors;
    @ApiModelProperty("理由")
    private String reason;
    @ApiModelProperty("物流单号")
    private String expressBill;
    @ApiModelProperty("状态")
    private Integer status;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date activeTime;
    private Long operatorId;
    private Long supplierId;
    private String express;
    @ApiModelProperty(value = "产品ID")
    private Long productId;

}