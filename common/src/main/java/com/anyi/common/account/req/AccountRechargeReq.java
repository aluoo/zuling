package com.anyi.common.account.req;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 资金明细列表接口请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@Data
@ApiModel
public class AccountRechargeReq {

    @NotNull(message = "充值金额不能为空")
    @ApiModelProperty(value = "充值金额")
    private Long rechargeAmount;

    @NotBlank(message = "凭证图片不能为空")
    @ApiModelProperty(value = "凭证图片")
    private String imageUrl;

    private String createBy;

    private Long companyId;


}
