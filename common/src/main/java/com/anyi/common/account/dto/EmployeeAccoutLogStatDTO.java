package com.anyi.common.account.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.anyi.common.account.constant.UserFocusTypeEnum;
import com.anyi.common.util.MoneyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAccoutLogStatDTO implements Serializable {
    @ApiModelProperty(value = "收入金额", example = "100.00")
    private Long income;
    @ApiModelProperty(value = "支出金额", example = "100.00")
    private Long expend;


    public String getIncome() {
        return income != null ? MoneyUtil.convert(this.income) : "0.00";
    }

    public String getExpend() {
        return expend != null ? MoneyUtil.convert(this.expend) : "0.00";
    }
}
