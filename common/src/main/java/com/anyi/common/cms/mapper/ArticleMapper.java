package com.anyi.common.cms.mapper;

import com.anyi.common.cms.domain.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    int increaseViewsById(@Param("id") Long id);
}