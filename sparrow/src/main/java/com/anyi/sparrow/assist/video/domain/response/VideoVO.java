package com.anyi.sparrow.assist.video.domain.response;

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
 * @Date 2023/8/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("发布时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

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