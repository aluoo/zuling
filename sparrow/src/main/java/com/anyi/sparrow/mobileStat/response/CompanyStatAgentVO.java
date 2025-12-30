package com.anyi.sparrow.mobileStat.response;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyStatAgentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("晒单总数")
    private Integer exchangeNum;
    @ApiModelProperty("代理团队")
    private List<AgencyStatVO> agencyList;
    @ApiModelProperty("直营门店晒单模块")
    private DirectCompanyStatVO directStatVO;




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AgencyStatVO implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("员工ID")
        private Long employeeId;
        @ApiModelProperty("公司ID")
        private Long companyId;
        @ApiModelProperty("员工名称")
        private String employeeName;
        @ApiModelProperty("部门名称")
        private String deptName;
        @ApiModelProperty("员工手机号")
        private String phone;
        @ApiModelProperty("部门管理员层级")
        private String ancestors;
        @ApiModelProperty("部门管理员Level")
        private int level;
        @ApiModelProperty("下一步是代理")
        private Boolean nextAgency;
        @ApiModelProperty("下一步是门店")
        private Boolean nextCompany;
        @ApiModelProperty("拉新订单数")
        private Integer exchangeAllNum;

        @ApiModelProperty("拉新晒单通过数")
        private Integer exchangePassNum;

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
    }


}
