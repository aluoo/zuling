package com.anyi.sparrow.notice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeUserMsgSendDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户id列表")
    private List<Long> userIds;

    @ApiModelProperty("消息类型(comm-普通消息,withdraw-提现)")
    private String bizType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("推送时间")
    private LocalDateTime pushTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("对象id")
    private Long bizId;
}