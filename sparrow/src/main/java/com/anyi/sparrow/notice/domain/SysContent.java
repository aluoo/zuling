package com.anyi.sparrow.notice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统发布公告表
 * </p>
 *
 * @author L
 * @since 2023-07-18
 */
@Getter
@Setter
@TableName("sys_content")
@ApiModel(value = "SysContent对象", description = "系统发布公告表")
public class SysContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("副标题摘要")
    private String digest;

    @ApiModelProperty("1重要2普通")
    private Integer type;

    @ApiModelProperty("内容详情")
    private String content;

    @ApiModelProperty("上传文件地址")
    private String picture;

    @ApiModelProperty("发布日期")
    private LocalDateTime pulishDate;

    @ApiModelProperty("状态1待发布2已发布3已停用")
    private Byte status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除 0否1是")
    private Boolean deleted;
}
