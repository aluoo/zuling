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
public class IncomeStatReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("1服务费2拉新3手机回收4享转数保")
    @NotNull(message = "类型不能为空")
    private Integer bizType;
}