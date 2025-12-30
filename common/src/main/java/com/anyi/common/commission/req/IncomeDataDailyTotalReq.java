package com.anyi.common.commission.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDataDailyTotalReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份 (yyyy年mm月), 不传默认返回当前月份数据")
    private String month;
}