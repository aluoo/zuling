package com.anyi.common.cms.domain.request;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

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
public class ArticleQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("第几页，从1开始，默认1")
    private int page = 1;

    @ApiModelProperty("每页条数，默认10")
    private int pageSize = 10;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("搜索内容")
    private String text;

    @ApiModelProperty(hidden = true)
    private Integer type;

    @ApiModelProperty(value = "业务类型", hidden = true)
    private String bizType;
}