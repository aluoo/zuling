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
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("发货中心回收商信息查询请求对象")
public class RecyclerListQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回收商ID")
    @NotNull(message = "回收商ID不能为空")
    private Long companyId;
}