package com.anyi.common.commission.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDataDailyDetailReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("每日时间 (收益统计列表原数据带过来)")
    @NotNull(message = "时间不能为空")
    private Date day;
}