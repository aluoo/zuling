package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
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
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("回收商取消抢单报价请求对象")
public class RecyclerCancelQuoteReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报价记录ID")
    @NotNull(message = "ID不能为空")
    private Long orderQuoteLogId;
    @ApiModelProperty("取消原因")
    private String reason;
    @ApiModelProperty("备注")
    private String remark;
}