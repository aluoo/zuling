package com.anyi.common.hk.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

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
@TableName("hk_notify_log")
@ApiModel(value = "号卡回调表")
@Data
public class HkNotifyLog extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("订单号")
    private String orderSn;
    @ApiModelProperty("三方订单号")
    private String thirdOrderSn;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("理由")
    private String reason;
    @ApiModelProperty("物流单号")
    private String expressBill;
    @ApiModelProperty("物流公司")
    private String express;
    @ApiModelProperty("激活状态")
    private String isActived;
    @ApiModelProperty("激活时间")
    private String activeTime;
    @ApiModelProperty("预约手机号")
    private String planMobileNumber;
    @ApiModelProperty("是否返回链接：1不返回，2返回链接")
    private String isReturnUrl;
    @ApiModelProperty("推送次数")
    private Integer num;
    @ApiModelProperty("回调报文JSON")
    private String data;




}