package com.anyi.common.address.vo;


import com.anyi.common.address.enums.AddrBizType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class GetLatestReq {
    @ApiModelProperty("业务类型 库存调拨:mov-order")
    private AddrBizType bizType;

    @ApiModelProperty("接收人员工id（如果收件人为自己不填，为别人就填别人）")
    private Long rspEmpId = -1l;
}
