package com.anyi.sparrow.common.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * "APP不需要分页信息，查询时不需要返回总条数，只需要返回数据即可
 * @param <T>
 */
@Deprecated
@Data
public class PageRes<T> {
    private Integer count;

    private List<T> data;

    public PageRes(PageInfo<T> pageInfo) {
        this.count = pageInfo.getSize();

        this.data = pageInfo.getList();
    }
}
