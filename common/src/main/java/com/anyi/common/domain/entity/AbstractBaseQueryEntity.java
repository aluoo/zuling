package com.anyi.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(value = { "handler", "fieldHandler"})
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页数", example = "1")
    @Builder.Default
    private int page = 1;

    @ApiModelProperty(value = "分页大小", example = "10")
    @Builder.Default
    private int pageSize = 10;
}