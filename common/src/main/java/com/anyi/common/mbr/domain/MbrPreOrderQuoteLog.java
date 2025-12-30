package com.anyi.common.mbr.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
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
@TableName("mbr_pre_order_quote_log")
@ApiModel(value = "租机平台方报价表")
@Data
public class MbrPreOrderQuoteLog extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("进件单Id")
    private Long orderId;

    @ApiModelProperty("订单状态-1已拒绝0待审核1审核通过")
    private Integer status;

    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;

    private Date quoteTime;

    private Long recyclerEmployeeId;

    private Long recyclerCompanyId;

    private Boolean quoted;





}