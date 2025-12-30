package com.anyi.sparrow.withdraw.req;

import cn.hutool.core.lang.RegexPool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/3/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ZfbBindReq {


    @NotEmpty(message = "卡号不能为空")
    @Pattern(regexp = "^(?:1[3-9]\\d{9}|[a-zA-Z\\d._-]*\\@[a-zA-Z\\d.-]{1,10}\\.[a-zA-Z\\d]{1,20})$", message = "支付宝账号格式不正确")
    @ApiModelProperty(value = "卡号")
    private String accountNo;
    @NotEmpty(message = "持卡人不能为空")
    @ApiModelProperty(value = "持卡人")
    private String ownerName;
    @NotEmpty(message = "身份证号不能为空")
    @Pattern(regexp = RegexPool.CITIZEN_ID, message = "身份证号格式不正确")
    @ApiModelProperty(value = "持卡人身份证")
    private String idCard;
}
