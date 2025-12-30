package com.anyi.common.exchange.response;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 拉新安装包
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Data
public class MbInstallVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("换机包ID")
    private Long exchangePhoneId;

    @ApiModelProperty("安装包名称")
    private String name;

    @ApiModelProperty("图标地址")
    private String iconUrl;

    @ApiModelProperty("打开时长")
    private Integer openTime;

    @ApiModelProperty("应用安装需要")
    private String applicationId;

    @ApiModelProperty("使用场景")
    private Integer type;

    @ApiModelProperty("渠道号")
    private String channelNo;

    @ApiModelProperty("RTA安装包token")
    private String channelToken;

    @ApiModelProperty("验新码地址")
    private String verifyUrl;

    @ApiModelProperty("下载地址")
    private String downUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("覆盖标识")
    private Boolean coverFlag;

}
