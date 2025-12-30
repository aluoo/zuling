package com.anyi.common.cms.domain.response;

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
 * @Date 2023/9/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章ID")
    private Long id;
    @ApiModelProperty("发布时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;
    @ApiModelProperty("分类ID")
    private Long categoryId;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("副标题")
    private String subTitle;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("内容")
    private String content;
}