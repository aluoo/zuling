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
public class MbrCreateShopDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺名称/业务员姓名")
    private String shopName;

    @ApiModelProperty("联系人姓名/业务员姓名")
    private String contactName;

    @ApiModelProperty("员工手机号/业务员手机号（店铺必须有一个员工），需要唯一")
    private String employeeMobile;

    @ApiModelProperty("安逸店铺id/业务员id，需要唯一")
    private String outShopId;

    @ApiModelProperty("类型（shop-门店，salesman-业务员）")
    private String type;

    @ApiModelProperty("    当type=employee时，必填，传入门店雇员归属的门店id，对应门店需已经开店")
    private String belongToOutShopId;

}