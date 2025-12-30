package com.anyi.sparrow.assist.video.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Date 2023/8/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("video")
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("是否删除")
    private Boolean deleted;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("创建者")
    private Long createBy;
    @ApiModelProperty("更新者")
    private Long updateBy;
    @ApiModelProperty("是否显示")
    private Boolean activated;
    @ApiModelProperty("推送时间")
    private Date publishTime;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("类型 1办理端app 2车主小程序")
    private Integer type;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("副标题")
    private String subTitle;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("视频地址")
    private String video;
    @ApiModelProperty("视频时长")
    private String videoLength;
    @ApiModelProperty("视频大小")
    private String videoSize;
}