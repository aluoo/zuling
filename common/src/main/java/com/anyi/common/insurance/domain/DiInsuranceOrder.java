package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.enums.DiOrderInsuranceStatusEnum;
import com.anyi.common.insurance.enums.DiOrderStatusEnum;
import com.anyi.common.insurance.enums.DiOrderSubStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("di_insurance_order")
@ApiModel(value = "数保订单表")
@Data
public class DiInsuranceOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;
    @ApiModelProperty("数保产品ID")
    private Long insuranceId;
    @ApiModelProperty("手机商品名称")
    private String productName;
    @ApiModelProperty("手机规格名称")
    private String productSpec;
    @ApiModelProperty("手机市场零售价")
    private Integer productSkuRetailPrice;
    @ApiModelProperty("手机发票购机价")
    private Integer productSkuPurchaseInvoicePrice;
    @ApiModelProperty("手机发票购机时间")
    private Date productSkuPurchaseInvoiceTime;
    @ApiModelProperty("数保产品年限")
    private Integer insurancePeriod;
    @ApiModelProperty("数保产品名称")
    private String insuranceName;
    @ApiModelProperty("数保产品类型")
    private String insuranceType;

    /**
     * @see DiOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;
    /**
     * @see DiOrderSubStatusEnum
     */
    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("手机串号")
    private String imeiNo;
    @ApiModelProperty("成交价")
    private Integer price;
    @ApiModelProperty("数保产品门店进价")
    private Integer insuranceNormalPrice;
    @ApiModelProperty("数保产品平台底价")
    private Long insuranceDownPrice;
    /**
     * @see DiOrderInsuranceStatusEnum
     */
    @ApiModelProperty("保险服务单状态")
    private Integer insuranceStatus;
    @ApiModelProperty("保险服务单号")
    private String insuranceNo;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("保险服务生效时间")
    private Date effectiveDate;
}