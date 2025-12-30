package com.anyi.common.mobileStat.response;

import cn.hutool.core.util.NumberUtil;
import com.anyi.common.domain.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 门店统计日看板表
 * </p>
 *
 * @author L
 * @since 2024-03-08
 */
@Data
public class CompanyStatVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("二手机交易数量")
    private Integer transNum;

    @ApiModelProperty("二手机询价数量")
    private Integer orderNum;

    @ApiModelProperty("二手机报价数量")
    private Integer priceNum;

    @ApiModelProperty("二手机成交金额")
    private Long finalPriceAmount;

    @ApiModelProperty("二手机收益金额")
    private Long commissionAmount;

    @ApiModelProperty("二手机取消订单数")
    private Integer cancelNum;

    @ApiModelProperty("二手机作废订单数")
    private Integer overtimeNum;

    @ApiModelProperty("二手机成交金额元")
    private String finalPriceAmountStr;

    @ApiModelProperty("二手机收益金额元")
    private String commissionAmountStr;

    @ApiModelProperty("拉新订单数")
    private Integer exchangeAllNum;

    @ApiModelProperty("换机晒单数")
    private Integer huanjiNum;

    @ApiModelProperty("换机晒单通过数")
    private Integer huanjiPassNum;

    @ApiModelProperty("换机晒单数")
    private String huanjiPassRate;

    @ApiModelProperty("抖音晒单数")
    private Integer appleNum;

    @ApiModelProperty("抖音晒单通过数")
    private Integer applePassNum;

    @ApiModelProperty("抖音晒单数")
    private String applePassRate;

    @ApiModelProperty("快手晒单数")
    private Integer lvzhouNum;

    @ApiModelProperty("快手晒单通过数")
    private Integer lvzhouPassNum;

    @ApiModelProperty("快手晒单数")
    private String lvzhouPassRate;

    @ApiModelProperty("拉新晒单通过数")
    private Integer exchangePassNum;

    @ApiModelProperty("数保全保数目")
    private Integer insuranceAnyNum;

    @ApiModelProperty("数保碎屏数目")
    private Integer insuranceSpNum;

    @ApiModelProperty("数保延保数目")
    private Integer insuranceYbNum;

    @ApiModelProperty("数保CARE数目")
    private Integer insuranceCareNum;

    @ApiModelProperty("数保安卓终身数目")
    private Integer insuranceAzNum;

    @ApiModelProperty("数保总数目")
    private Integer insuranceALLNum;


    public Integer getInsuranceALLNum(){
        return insuranceAnyNum+insuranceSpNum+insuranceYbNum+insuranceCareNum+insuranceAzNum;
    }


    public String  getHuanjiPassRate(){
        if(huanjiNum==0) return "0.00";
        BigDecimal rate = NumberUtil.div(new BigDecimal(huanjiPassNum),new BigDecimal(huanjiNum),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();

    }

    public String  getApplePassRate(){
        if(appleNum==0) return "0.00";
        BigDecimal rate = NumberUtil.div(new BigDecimal(applePassNum),new BigDecimal(appleNum),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();

    }

    public String  getLvzhouPassRate(){
        if(lvzhouNum==0) return "0.00";
        BigDecimal rate = NumberUtil.div(new BigDecimal(lvzhouPassNum),new BigDecimal(lvzhouNum),2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        return rate.toString();

    }


    public Integer getExchangePassNum(){
        return huanjiPassNum+lvzhouPassNum+applePassNum;
    }

}
