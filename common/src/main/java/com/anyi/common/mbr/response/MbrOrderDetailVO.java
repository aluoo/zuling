package com.anyi.common.mbr.response;

import cn.hutool.core.util.EnumUtil;
import com.anyi.common.mbr.enums.MbrOrderStatusEnum;
import com.anyi.common.mbr.enums.MbrPreOrderStatusEnum;
import com.anyi.common.mbr.enums.MbrPreOrderSubStatusEnum;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
public class MbrOrderDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("门店员工名称")
    private String storeEmployeeName;

    @ApiModelProperty("名称")
    private String storeCompanyName;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("员工手机号")
    private String storeEmployeeMobile;

    @ApiModelProperty("第三方订单号")
    private String thirdOrderId;

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
    /**
     * @see MbrOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("结算金额")
    private Long settleAmount;

    @ApiModelProperty("方案金额")
    private Long planAmount;

    @ApiModelProperty("押金金额")
    private Long depositAmount;

    @ApiModelProperty("状态中文")
    private String statusName;

    @ApiModelProperty("备注")
    private String remark;

    public String getSettleAmount() {
       return MoneyUtil.convert(settleAmount);
    }


    public String getStatusName(){
        return EnumUtil.getBy(MbrOrderStatusEnum::getCode,this.status).getDesc();
    }
}