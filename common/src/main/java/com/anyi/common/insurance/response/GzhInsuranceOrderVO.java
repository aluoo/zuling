package com.anyi.common.insurance.response;

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
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GzhInsuranceOrderVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("保险订单ID")
    private Long orderId;

    @ApiModelProperty("报险单编号")
    private String orderNo;

    @ApiModelProperty("数保产品ID")
    private Long insuranceId;

    @ApiModelProperty("数保产品名称")
    private String insuranceName;

    @ApiModelProperty("数保产品类型")
    private String insuranceType;

    @ApiModelProperty(value = "手机型号")
    private String phoneName;

    @ApiModelProperty("投保说明")
    private String remark;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("状态中文")
    private String statusName;

    @ApiModelProperty(value = "imeiNo")
    private String imeiNo;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("失效时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    @ApiModelProperty("我要报修,修改报修申请")
    private Boolean fixButton;
    @ApiModelProperty("上传理赔资料按钮")
    private Boolean saveDataButton;
    @ApiModelProperty("修改理赔资料按钮")
    private Boolean updateDataButton;

    @ApiModelProperty("数保年限")
    private Integer insurancePeriod;

    @ApiModelProperty("保险服务单状态")
    private Integer insuranceStatus;

    @ApiModelProperty("迭代手机商品名称")
    private String productName;
    @ApiModelProperty("迭代手机规格名称")
    private String productSpec;
    @ApiModelProperty("迭代升级手机市场零售价")
    private Integer productSkuRetailPrice;
    @ApiModelProperty("旧机手机市场零售价")
    private Integer oldSkuRetailPrice;
    @ApiModelProperty("补交金额")
    private Integer suppleAmount;
    @ApiModelProperty("理赔金额")
    private Integer settleAmount;
    @ApiModelProperty("折抵价格")
    private Integer discountAmount;
    @ApiModelProperty(value = "手机型号")
    private String newPhoneName;

    @ApiModelProperty(value = "服务类型")
    private Long serviceType;
    @ApiModelProperty(value = "服务类型")
    private Long claimItem;

    @ApiModelProperty(value = "服务类型中文")
    private String serviceTypeName;
    @ApiModelProperty(value = "理赔项目中文")
    private String claimItemName;
    @ApiModelProperty(value = "迭代升级")
    private Integer upProduct;
    @ApiModelProperty(value = "迭代升级中文")
    private String upProductName;
    @ApiModelProperty(value = "可调出串号")
    private Boolean imeiRead;
    @ApiModelProperty(value = "可调出串号中文")
    private String imeiReadName;

    @ApiModelProperty(value = "手机故障")
    private String breakDown;

    @ApiModelProperty(value = "失败原因")
    private String failRemark;


    public String getProductSkuRetailPrice(){
       if(productSkuRetailPrice == null) return "";
       return MoneyUtil.fenToYuan(productSkuRetailPrice);
    }

    public String getOldSkuRetailPrice(){
        if(oldSkuRetailPrice == null) return "";
        return MoneyUtil.fenToYuan(oldSkuRetailPrice);
    }

    public String getSuppleAmount(){
        if(suppleAmount == null) return "";
        return MoneyUtil.fenToYuan(suppleAmount);
    }

    public String getDiscountAmount(){
        if(discountAmount == null) return "";
        return MoneyUtil.fenToYuan(discountAmount);
    }

    public String getSettleAmount(){
        if(settleAmount == null) return "";
        return MoneyUtil.fenToYuan(settleAmount);
    }


}