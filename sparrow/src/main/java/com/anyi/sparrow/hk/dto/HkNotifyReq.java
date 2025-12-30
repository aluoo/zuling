package com.anyi.sparrow.hk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HkNotifyReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private String order_sn;
    @ApiModelProperty("三方订单号")
    private String third_order_sn;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("理由")
    private String reason;
    @ApiModelProperty("物流单号")
    private String express_bill;
    @ApiModelProperty("物流公司")
    private String express;
    @ApiModelProperty("激活状态")
    private String is_actived;
    @ApiModelProperty("激活时间")
    private String active_time;
    @ApiModelProperty("预约手机号")
    private String plan_mobile_number;
    @ApiModelProperty("是否返回链接：1不返回，2返回链接")
    private String is_return_url;
    @ApiModelProperty("推送次数")
    private Integer num;




}