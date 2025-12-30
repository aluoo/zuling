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
 * @Date 2024/5/17
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmsIndexEntryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("展示位置 1首页 2换机首页")
    private Integer place;
}