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
 * 晒单客户机基础信息
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Getter
@Setter
@TableName("mb_exchange_device")
@ApiModel(value = "MbExchangeDevice对象", description = "晒单客户机基础信息")
public class MbExchangeDevice extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("出厂日期")
    private Date factoryDate;

    @ApiModelProperty("安装日期")
    private Date installDate;

    @ApiModelProperty("开机时长秒")
    private Long openTime;

    @ApiModelProperty("机器IP")
    private String ip;
    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("系统版本号")
    private String sysVersion;

    @ApiModelProperty("安卓版本号")
    private String androidVersion;

    @ApiModelProperty("安卓手机标识")
    private String oaid;


}
