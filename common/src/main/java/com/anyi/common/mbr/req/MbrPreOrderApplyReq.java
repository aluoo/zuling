package com.anyi.common.mbr.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbrPreOrderApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品SKU ID")
    @NotNull(message = "规格ID不能为空")
    public Long skuId;

    @ApiModelProperty("客户姓名")
    @NotBlank(message = "姓名不能为空")
    private String customName;

    @ApiModelProperty("客户手机号")
    @NotBlank(message = "手机号不能为空")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    @NotBlank(message = "身份证不能为空")
    private String idCard;

    @ApiModelProperty("1新机2二手机")
    private Integer productType;

    @ApiModelProperty("期数")
    private Integer period;




}