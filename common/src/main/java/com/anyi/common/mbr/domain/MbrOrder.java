package com.anyi.common.mbr.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.mbr.enums.MbrOrderStatusEnum;
import com.anyi.common.mbr.enums.MbrOrderSubStatusEnum;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("mbr_order")
@ApiModel(value = "租机单表")
@Data
public class MbrOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty("门店ID")
    private Long storeCompanyId;
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
    /**
     * @see MbrOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;
    /**
     * @see MbrOrderSubStatusEnum
     */
    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;

    @ApiModelProperty("商品成本价格")
    private Long settleAmount;

    @ApiModelProperty("方案价格")
    private Long planAmount;

    @ApiModelProperty("押金价格")
    private Long depositAmount;

    @ApiModelProperty("层级")
    private String ancestors;


}