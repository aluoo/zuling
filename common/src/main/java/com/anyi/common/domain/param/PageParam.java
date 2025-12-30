package com.anyi.common.domain.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PageParam<T> implements Serializable {

    private static final Integer DEFAULT_CURRENT = 1;
    private static final Integer DEFAULT_SIZE = 10;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer current;

    /**
     * 每页大小
     */
    @ApiModelProperty("每页数据量")
    private Integer size;

    private T param;

    public Integer getCurrent() {
        if (current == null || current < DEFAULT_CURRENT) {
            return DEFAULT_CURRENT;
        }
        return current;
    }

    public Integer getSize() {
        if (size == null || size < DEFAULT_SIZE) {
            return DEFAULT_SIZE;
        }
        return size;
    }
}
