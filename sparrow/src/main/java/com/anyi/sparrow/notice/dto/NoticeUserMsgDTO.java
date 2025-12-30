package com.anyi.sparrow.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeUserMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("消息类型(comm-普通消息,withdraw-提现)")
    private String bizType;

    @ApiModelProperty("消息展示的类型名称")
    private String typeName;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("摘要")
    private String digest;

    @ApiModelProperty("推送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushTime;

    @ApiModelProperty("是否已读(0-未读,1-已读)")
    private Integer hasRead;

    @ApiModelProperty("已读时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date readTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("图片")
    private String picture;

    @ApiModelProperty("对象id")
    private Long bizId;


    @ApiModelProperty("重要公告标识1重要0普通")
    private Boolean importFlag;
}