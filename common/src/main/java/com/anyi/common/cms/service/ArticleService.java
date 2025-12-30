package com.anyi.common.cms.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.cms.domain.Article;
import com.anyi.common.cms.domain.request.ArticleQueryReq;
import com.anyi.common.cms.domain.request.ArticleIdReq;
import com.anyi.common.cms.domain.response.ArticleVO;
import com.anyi.common.cms.mapper.ArticleMapper;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Slf4j
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {
    @Autowired
    private ViewsService viewsService;

    public List<ArticleVO> listArticleByCategoryId(ArticleQueryReq req) {
        if (req.getCategoryId() == null) {
            return new ArrayList<>();
        }
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Article> list = this.lambdaQuery()
                .eq(Article::getCategoryId, req.getCategoryId())
                .eq(Article::getDeleted, false)
                .eq(Article::getActivated, true)
                .like(StrUtil.isNotBlank(req.getText()), Article::getTitle, req.getText())
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getIsPopular)
                .orderByDesc(Article::getViews)
                .orderByDesc(Article::getPublishTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(list, ArticleVO.class);
    }

    public ArticleVO detailArticleById(ArticleIdReq req) {
        if (req.getArticleId() == null) {
            return null;
        }
        Article bean = this.getById(req.getArticleId());
        ArticleVO vo = ArticleVO.builder().build();
        BeanUtil.copyProperties(bean, vo);
        viewsService.increaseViews(req.getArticleId());
        return vo;
    }
}