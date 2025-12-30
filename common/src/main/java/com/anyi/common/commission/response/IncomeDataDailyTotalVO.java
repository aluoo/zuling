package com.anyi.common.commission.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
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
@ApiModel(value = "收益统计响应对象")
public class IncomeDataDailyTotalVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份 (yyyy年MM月)")
    private String month;

    @ApiModelProperty("共计")
    private String totalValue;

    @ApiModelProperty("数据列表")
    private List<DailyData> list;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel(value = "收益统计数据响应对象")
    public static class DailyData implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("每日时间 (不展示，用于明细接口传参)")
        private Date day;
        @ApiModelProperty("标题")
        private String title;
        @ApiModelProperty("金额")
        private String value;
    }
}