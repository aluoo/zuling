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
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDataDailyDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日期")
    private String day;

    @ApiModelProperty("共计")
    private String totalValue;

    @ApiModelProperty("个人佣金列表")
    private List<DetailData> self;
    @ApiModelProperty("个人佣金共计")
    private String selfTotalValue;

    @ApiModelProperty("团队佣金列表")
    private List<DetailData> teams;
    @ApiModelProperty("团队佣金共计")
    private String teamsTotalValue;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailData implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("标题")
        private String title;
        @ApiModelProperty("金额")
        private String value;
        @ApiModelProperty("明细ID，用于详情明细页面传参")
        private Long id;
    }
}