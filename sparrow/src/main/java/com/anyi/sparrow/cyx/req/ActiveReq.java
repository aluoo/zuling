package com.anyi.sparrow.cyx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 接口请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/11/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ActiveReq implements Serializable {
    private static final long serialVersionUID = 1L;
/*
    {
        "orderNo": "2343434343434343434344",
            "ownerName":"陈XX",
            "driverPhone":"18959680342",
            "obuld":"3701000000000",
            "cardId":"3701000000000",
            "plateNum":"闽DDDDD",
            "plateColor":"蓝",
            "activeTime":"2023-09-12 15:03:03"
    }
    */

    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "车主姓名")
    private String ownerName;
    @ApiModelProperty(value = "车主手机号码")
    private String  driverPhone;
    @ApiModelProperty(value = "obu合同序列号")
    private String  obuId;

    @ApiModelProperty(value = "ic,卡号编码")
    private String  cardId;

    @ApiModelProperty(value = "车牌号")
    private String  plateNum;

    @ApiModelProperty(value = "车牌颜色")
    private String  plateColor;

    @ApiModelProperty(value = "激活时间")
    private String  activeTime;

    @ApiModelProperty(value = "发行方类型,无需三方传自己设置")
    private Long etcTypeId;
}