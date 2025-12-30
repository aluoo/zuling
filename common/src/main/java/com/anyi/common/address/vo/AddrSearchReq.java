package com.anyi.common.address.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class AddrSearchReq implements Serializable {

    @ApiModelProperty("搜索key")
    private String searchKey;
}
