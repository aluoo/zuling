package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数保报修订单
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_insurance_fix_order")
@ApiModel(value = "DiInsuranceFixOrder对象", description = "数保报修订单")
public class DiInsuranceFixOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("投保单ID")
    private Long orderId;

    @ApiModelProperty("报险用户手机")
    private String mobile;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("服务类型ID")
    private Long serviceType;

    @ApiModelProperty("理赔项目ID")
    private Long claimItem;

    @ApiModelProperty("是否读取串号")
    private Boolean imeiRead;

    @ApiModelProperty("手机故障")
    private String breakDown;

    @ApiModelProperty("迭代升级1同款同型2升级迭代")
    private Integer upProduct;

    @ApiModelProperty("理赔金额")
    private Integer settleAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("维修收款账号姓名")
    private String fixName;

    @ApiModelProperty("维修收款支付宝账号")
    private String fixAlipay;

    @ApiModelProperty("维修银行卡号")
    private String fixBankAccount;

    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("手机市场零售价")
    private Integer productSkuRetailPrice;

    @ApiModelProperty("旧机手机市场零售价")
    private Integer oldSkuRetailPrice;

    @ApiModelProperty("补交金额")
    private Integer suppleAmount;

    @ApiModelProperty("折抵价格")
    private Integer discountAmount;

    @ApiModelProperty("订单编码")
    private String orderNo;

    @ApiModelProperty("维修城市")
    private String fixCity;

    @ApiModelProperty("员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("联系人手机号码")
    private String  contactMobile;

    @ApiModelProperty("换新机器的IMEI")
    private String newImei;


}
