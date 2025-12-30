package com.anyi.sparrow.assist.video.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/3
 * @Copyright 盒物科技
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("第几页，从1开始，默认1")
    private int page = 1;

    @ApiModelProperty("每页条数，默认10")
    private int pageSize = 10;
}