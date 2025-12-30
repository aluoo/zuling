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
 * 晒单图片信息表
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Getter
@Setter
@TableName("mb_exchange_pic")
@ApiModel(value = "MbExchangePic对象", description = "晒单图片信息表")
public class MbExchangePic extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("安装包渠道编码")
    private String installChannelNo;

    @ApiModelProperty("类似抖音UID")
    private String uid;

    @ApiModelProperty("类似抖音DID")
    private String did;

    @ApiModelProperty("类似抖音ACTTIME")
    private Date actTime;

    @ApiModelProperty("类似抖音CHANNEL")
    private String installChannel;

    @ApiModelProperty("图片照片")
    private String imageUrl;

    @ApiModelProperty("拉新安装包ID")
    private Long installId;

    @ApiModelProperty("拉新安装包名称")
    private String installName;

    @ApiModelProperty("IMEI")
    private String imeiNo;

    @ApiModelProperty("串码")
    private String serino;

}
