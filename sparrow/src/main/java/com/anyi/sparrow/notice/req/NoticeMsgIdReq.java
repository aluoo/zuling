package com.anyi.sparrow.notice.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class NoticeMsgIdReq {

    @ApiModelProperty(value = "消息ID", dataType = "long")
    private Long msgId;


}
