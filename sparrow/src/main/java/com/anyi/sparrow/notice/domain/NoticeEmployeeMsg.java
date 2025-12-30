package com.anyi.sparrow.notice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知_员工消息
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
@Getter
@Setter
@TableName("notice_employee_msg")
@ApiModel(value = "NoticeEmployeeMsg对象", description = "通知_员工消息")
public class NoticeEmployeeMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("消息类型(comm-普通消息,withdraw-提现)")
    private String bizType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("摘要")
    private String digest;

    @ApiModelProperty("推送时间")
    private Date pushTime;

    @ApiModelProperty("是否已读(0-未读,1-已读)")
    private Boolean hasRead;

    @ApiModelProperty("已读时间")
    private Date readTime;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("图片")
    private String picture;

    @ApiModelProperty("对象id")
    private Long bizId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
