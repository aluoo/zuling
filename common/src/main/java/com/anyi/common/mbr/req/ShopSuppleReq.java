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
public class ShopSuppleReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺ID")
    private Long outShopId;

    @ApiModelProperty("营业执照上的门店名称")
    private String businessLicenseName;

    @ApiModelProperty("营业执照上的统一社会信用代码")
    private String businessLicenseNo;

    @ApiModelProperty("营业执照上的经营者姓名")
    private String shopLegalName;

    @ApiModelProperty("收款人完整姓名")
    private String payee;

    @ApiModelProperty("收款人支付宝账号")
    private String account;


}