package com.anyi.common.insurance.domain.dto;

import com.anyi.common.insurance.enums.DiOrderPictureTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiInsuranceOrderPictureDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("数保订单ID")
    private Long insuranceOrderId;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("地址")
    private String url;
    /**
     * @see DiOrderPictureTypeEnum
     */
    @ApiModelProperty("类型")
    private Integer type;
}