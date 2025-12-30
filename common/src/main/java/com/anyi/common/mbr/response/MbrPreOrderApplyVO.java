package com.anyi.common.mbr.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbrPreOrderApplyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("进件单ID")
    private Long preOrderId;

    @ApiModelProperty("租机单ID")
    private Long orderId;

}