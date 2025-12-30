package com.anyi.common.cms.service;

import com.anyi.common.cms.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/13
 */
@Slf4j
@Service
public class ViewsService {
    @Autowired
    private ArticleMapper articleMapper;

    @Async
    public void increaseViews(Long id) {
        if (id == null) {
            return;
        }
        articleMapper.increaseViewsById(id);
    }
}