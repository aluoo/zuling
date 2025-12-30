package com.anyi.common.address.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteReq {
    @ApiModelProperty("地址id")
    private Long addressId;
}
