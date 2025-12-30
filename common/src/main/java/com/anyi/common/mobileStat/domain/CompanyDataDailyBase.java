package com.anyi.common.mobileStat.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 门店统计日看板表
 * </p>
 *
 * @author L
 * @since 2024-03-08
 */
@Getter
@Setter
@TableName("company_data_daily_base")
@ApiModel(value = "CompanyDataDailyBase对象", description = "门店统计日看板表")
public class CompanyDataDailyBase extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("交易数量")
    private Integer transNum;

    @ApiModelProperty("询价数量")
    private Integer orderNum;

    @ApiModelProperty("报价数量")
    private Integer priceNum;

    @ApiModelProperty("成交金额")
    private Long finalPriceAmount;

    @ApiModelProperty("收益金额")
    private Long commissionAmount;

    @ApiModelProperty("取消订单数")
    private Integer cancelNum;

    @ApiModelProperty("作废订单数")
    private Integer overtimeNum;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("每天0点")
    private Date day;

    @ApiModelProperty("拉新晒单数目")
    private Integer exchangeNum;
    @ApiModelProperty("拉新晒单成功数")
    private Integer exchangePassNum;
    @ApiModelProperty("数保全保投保数目")
    private Integer insuranceAnyNum;
    @ApiModelProperty("数保碎屏投保数目")
    private Integer insuranceSpNum;
    @ApiModelProperty("数保延长保数目")
    private Integer insuranceYbNum;
    @ApiModelProperty("数保CARE投保数目")
    private Integer insuranceCareNum;
    @ApiModelProperty("数保安卓终身保投保数目")
    private Integer insuranceAzNum;


}
