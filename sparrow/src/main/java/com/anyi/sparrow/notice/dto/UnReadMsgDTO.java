package com.anyi.sparrow.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class UnReadMsgDTO implements Serializable {

    @ApiModelProperty(value = "消息id", dataType = "long")
    private Long msgId;

    @ApiModelProperty(value = "消息类型(comm-普通消息,withdraw-提现)")
    private String bizType;

    @ApiModelProperty(value = "标题")
    private String msg;

//    @ApiModelProperty("消息内容")
//    private String content;

    @ApiModelProperty(value = "对象id")
    private Long bizId;
}
