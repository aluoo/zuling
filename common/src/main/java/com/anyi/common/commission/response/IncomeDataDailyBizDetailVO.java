package com.anyi.common.commission.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDataDailyBizDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日期")
    private String day;

    @ApiModelProperty("共计")
    private String totalValue;

    @ApiModelProperty("页面标题")
    private String pageTitle;

    @ApiModelProperty("获得类型(1-个人、2-团队贡献、3-团队总计)")
    private Integer gainType;
    @ApiModelProperty("结算业务类型(1-推广、2-服务费、3-奖金)")
    private Integer bizType;

    @ApiModelProperty("数据列表")
    private List<Detail> list;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Detail implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("标题")
        private String title;
        @ApiModelProperty("金额")
        private String value;
        @ApiModelProperty("激活数量")
        private Integer activeNum;
    }
}