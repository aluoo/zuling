package com.anyi.common.mbr.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.mbr.enums.MbrPreOrderStatusEnum;
import com.anyi.common.mbr.enums.MbrPreOrderSubStatusEnum;
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
@TableName("mbr_pre_order")
@ApiModel(value = "租机进件表")
@Data
public class MbrPreOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty("门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;
    @ApiModelProperty("手机商品名称")
    private String productName;
    @ApiModelProperty("手机规格名称")
    private String productSpec;
    @ApiModelProperty("1新机2二手机")
    private Integer productType;
    @ApiModelProperty("期数")
    private Integer period;
    @ApiModelProperty("客户姓名")
    private String customName;
    @ApiModelProperty("客户手机号")
    private String customPhone;
    @ApiModelProperty("客户身份证")
    private String idCard;
    /**
     * @see MbrPreOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;
    /**
     * @see MbrPreOrderSubStatusEnum
     */
    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;

    private Long recyclerEmployeeId;

    private Long recyclerCompanyId;

    private Long quoteLogId;

    private Date finishQuoteTime;

    private Boolean quotable;

}