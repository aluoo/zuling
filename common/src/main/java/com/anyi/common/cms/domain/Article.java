package com.anyi.common.cms.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("cms_article")
public class Article extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("发布时间")
    private Date publishTime;
    @ApiModelProperty("是否显示 0隐藏1显示")
    private Boolean activated;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("分类ID")
    private Long categoryId;
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
    @ApiModelProperty("是否热门")
    private Boolean isPopular;
    @ApiModelProperty("是否置顶")
    private Boolean isTop;
    @ApiModelProperty("阅读数")
    private Long views;
}