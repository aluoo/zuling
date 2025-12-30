package com.anyi.common.mbr.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MbrCreateOrderNotifyReq  implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一标识")
    private Long storeEmployeeId;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("第三方订单号")
    private Long thirdOrderId;

    @ApiModelProperty("新机二手机")
    private String productType;

    @ApiModelProperty("期数")
    private Integer period;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("商品成本价格")
    private Long settleAmount;

    @ApiModelProperty("方案价格")
    private Long planAmount;

    @ApiModelProperty("押金价格")
    private Long depositAmount;

    @ApiModelProperty("结算地址")
    private String settleLink;


}