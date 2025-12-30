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
public class BandCardBindReq {

    @ApiModelProperty(value = "发卡银行")
    @NotEmpty(message = "发卡银行名称不能为空")
    private String accountName;
    @NotEmpty(message = "卡号不能为空")
//    @Pattern(regexp = "^(\\d{16}|\\d{19})$",message = "卡号格式不正确")
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
