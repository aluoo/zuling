package com.anyi.sparrow.notice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("notice_user_msg")
public class NoticeUserMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("消息类型(comm-普通消息,withdraw-提现)")
    private String bizType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("摘要")
    private String digest;

    @ApiModelProperty("推送时间")
    private LocalDateTime pushTime;

    @ApiModelProperty("是否已读(0-未读,1-已读)")
    private Integer hasRead;

    @ApiModelProperty("已读时间")
    private LocalDateTime readTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("对象id")
    private Long bizId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}