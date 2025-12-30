package com.anyi.common.product.domain.request;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发货中心回收商订单信息查询请求对象")
public class RecyclerOrderQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回收商ID", required = true)
    @NotNull(message = "回收商ID不能为空")
    private Long companyId;

    @ApiModelProperty(value = "是否核验")
    private Boolean verified;

    @ApiModelProperty(value = "订单码")
    private String orderNo;
}