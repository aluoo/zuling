package com.anyi.common.exchange.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 换机晒单表
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Getter
@Setter
@TableName("mb_exchange_order")
@ApiModel(value = "MbExchangeOrder对象", description = "换机晒单表")
public class MbExchangeOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("店员ID")
    private Long storeEmployeeId;

    @ApiModelProperty("订单类型3换机4拉新5")
    private Integer type;

    @ApiModelProperty("来源0外部渠道1系统识别2人工介入")
    private Integer source;

    @ApiModelProperty("合伙人ID")
    private Long bdId;

    @ApiModelProperty("区域经理ID")
    private Long areaId;

    @ApiModelProperty("代理ID")
    private Long agentId;

    @ApiModelProperty("手机系统")
    private String sysMobile;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("IMEI号")
    private String imeiNo;
    @ApiModelProperty("安卓标识")
    private String oaid;

    @ApiModelProperty("换机包编码")
    private String exchangePhoneNo;

    @ApiModelProperty("审核状态")
    private Integer status;

    @ApiModelProperty("结算状态")
    private Integer settleStatus;

    @ApiModelProperty("审核时间")
    private Date trialTime;

    @ApiModelProperty("审核备注")
    private String remark;

    @ApiModelProperty("机审核状态")
    private Integer sysStatus;

    @ApiModelProperty("审核备注")
    private String sysRemark;

    @ApiModelProperty("平台审核表示")
    private Boolean platCheck;
    @ApiModelProperty("异常标识")
    private Boolean illegal;

    @ApiModelProperty("渠道码,平台方提供")
    private String channelCode;

    @ApiModelProperty("渠道名称")
    private String channelName;

    @ApiModelProperty("渠道方自己的唯一订单号")
    private String channelOrderNo;



}
