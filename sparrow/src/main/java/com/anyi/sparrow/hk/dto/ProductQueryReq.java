package com.anyi.sparrow.hk.dto;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/7/28
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQueryReq extends AbstractBaseQueryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("运营商ID")
    private Long operatorId;

    @ApiModelProperty("产品名称")
    private String productName;


}