package com.anyi.common.mbr.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class MbrShopCodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付宝二维码地址URL")
    private String qrcodeUrl;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("荟玩租店铺ID")
    private Long shopId;

    @ApiModelProperty("安逸店铺id/业务员id，需要唯一")
    private String outShopId;


}