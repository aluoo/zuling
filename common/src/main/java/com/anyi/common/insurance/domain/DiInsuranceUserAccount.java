package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数保用户表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_insurance_user_account")
@ApiModel(value = "DiInsuranceUserAccount对象", description = "数保用户表")
public class DiInsuranceUserAccount extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("身份证号")
    private Long idCard;

    @ApiModelProperty("密码")
    private String passWord;

    @ApiModelProperty("1正常2注销3下线")
    private Byte status;

    @ApiModelProperty("密码")
    private String name;
}
