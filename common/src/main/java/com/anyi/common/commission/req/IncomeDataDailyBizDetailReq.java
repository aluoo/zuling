package com.anyi.common.commission.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
public class IncomeDataDailyBizDetailReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID (结算明细列表原数据带过来)")
    @NotNull(message = "ID不能为空")
    private Long id;
}