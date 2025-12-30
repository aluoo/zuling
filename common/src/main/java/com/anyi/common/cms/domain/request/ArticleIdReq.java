package com.anyi.common.cms.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleIdReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章ID")
    private Long articleId;
}