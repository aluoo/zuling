package com.anyi.sparrow.cms.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.cms.domain.CmsCategory;
import com.anyi.common.cms.domain.enums.IconPlaceEnum;
import com.anyi.common.cms.domain.request.ArticleIdReq;
import com.anyi.common.cms.domain.request.ArticleQueryReq;
import com.anyi.common.cms.domain.request.CmsIndexEntryReq;
import com.anyi.common.cms.domain.response.ArticleVO;
import com.anyi.common.cms.domain.response.CategoryVO;
import com.anyi.common.cms.domain.response.CmsIndexIconVO;
import com.anyi.common.cms.service.ArticleService;
import com.anyi.common.cms.service.CmsCategoryService;
import com.anyi.common.cms.service.CmsIndexIconService;
import com.anyi.common.cms.service.ViewsService;
import com.anyi.common.domain.param.Response;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.employee.service.ReviewStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/15
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "内容管理-知识库&常见问题")
@RestController
@RequestMapping("/mobile/cms")
public class CmsController {
    @Autowired
    private CmsCategoryService cmsCategoryService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ViewsService viewsService;
    @Autowired
    private CommonSysDictService sysDictService;
    @Autowired
    private CmsIndexIconService cmsIndexIconService;
    @Autowired
    private ReviewStatusService reviewStatusService;

    @ApiOperationSupport(order = 0)
    @ApiOperation("获取分类列表")
    @WebLog(description = "内容管理-获取分类列表")
    @RequestMapping(value = "/category/list", method = RequestMethod.POST)
    public Response<List<CategoryVO>> listCategories() {
        List<CmsCategory> list = cmsCategoryService.lambdaQuery()
                .eq(CmsCategory::getDeleted, false)
                .eq(CmsCategory::getActivated, true)
                .orderByDesc(CmsCategory::getSort)
                .list();
        if (CollUtil.isEmpty(list)) {
            list = new ArrayList<>();
        }
        List<CategoryVO> resp = BeanUtil.copyToList(list, CategoryVO.class);

        return Response.ok(resp);
    }

    @ApiOperationSupport(order = 1)
    @ApiOperation("获取文章列表")
    @WebLog(description = "内容管理-获取文章列表")
    @RequestMapping(value = "/article/list", method = RequestMethod.POST)
    public Response<List<ArticleVO>> listArticleByCategoryId(@RequestBody ArticleQueryReq req) {
        return Response.ok(articleService.listArticleByCategoryId(req));
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation("根据ID获取文章详情")
    @WebLog(description = "内容管理-根据ID获取文章详情")
    @RequestMapping(value = "/article/detail", method = RequestMethod.POST)
    public Response<ArticleVO> detailArticleById(@RequestBody ArticleIdReq req) {
        return Response.ok(articleService.detailArticleById(req));
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation("根据ID增加文章阅读数")
    @WebLog(description = "内容管理-根据ID增加文章阅读数")
    @RequestMapping(value = "/article/view", method = RequestMethod.POST)
    public Response<?> viewArticleById(@RequestBody ArticleIdReq req) {
        viewsService.increaseViews(req.getArticleId());
        return Response.ok();
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation("获取首页banner列表")
    @WebLog(description = "内容管理-获取首页banner列表")
    @RequestMapping(value = "/index/banner/temporary", method = RequestMethod.POST)
    public Response<List<String>> indexBanner() {
        return Response.ok(sysDictService.getIndexBanner());
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation("获取首页入口列表")
    @WebLog(description = "内容管理-获取首页入口列表")
    @RequestMapping(value = "/index/icon/entry/temporary", method = RequestMethod.POST)
    public Response<List<CmsIndexIconVO>> indexIconEntry() {
        boolean reStatus = reviewStatusService.getReStatus(LoginUserContext.getUser().getId());
        return Response.ok(cmsIndexIconService.listIconsByPlace(IconPlaceEnum.INDEX.getType(), null, reStatus,LoginUserContext.getUser().getCompanyType()));
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation("根据展示位置获取入口Icon列表")
    @WebLog(description = "内容管理-根据展示位置获取入口Icon列表")
    @RequestMapping(value = "/icon/entry/by_place", method = RequestMethod.POST)
    public Response<List<CmsIndexIconVO>> getIconByPlace(@RequestBody CmsIndexEntryReq req) {
        boolean reStatus = reviewStatusService.getReStatus(LoginUserContext.getUser().getId());
        return Response.ok(cmsIndexIconService.listIconsByPlace(req.getPlace(), LoginUserContext.getUser().getCompanyId(), reStatus,LoginUserContext.getUser().getCompanyType()));
    }
}