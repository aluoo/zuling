package com.anyi.common.product.domain.request;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("回收商报价中心报价单列表请求对象")
public class RecyclerQuoteLogListReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态 0待报价 1报价中 2已报价 -1已作废")
    private Integer status;

    @ApiModelProperty(value = "只查看自己，不传默认查看全部", example = "false")
    @Builder.Default
    private Boolean onlySelf = false;
}