package com.anyi.common.exchange.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 拉新安装包
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_verify_install")
public class MbVerifyInstall extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("安装包名称")
    private String name;

    @ApiModelProperty("安装包id")
    private String applicationId;

    @ApiModelProperty("渠道号")
    private String channelNo;
    @ApiModelProperty("类型编码")
    private String typeCode;
    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("验新码地址")
    private String verifyUrl;

    @ApiModelProperty("下载地址")
    private String downUrl;

    @ApiModelProperty("安装包图标")
    private String iconUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态")
    private Integer status;


}
