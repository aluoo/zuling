package com.anyi.sparrow.product.controller;

import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.request.CategoryQueryReq;
import com.anyi.common.product.domain.request.ProductQueryReq;
import com.anyi.common.product.domain.response.CategoryVO;
import com.anyi.common.product.domain.response.ProductDetailVO;
import com.anyi.common.product.domain.response.ProductSkuVO;
import com.anyi.common.product.domain.response.ProductVO;
import com.anyi.common.service.ProductManageService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/30
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "商品管理")
@RestController
@RequestMapping("/mobile/product")
public class ProductController {
    @Autowired
    private ProductManageService service;

    @ApiOperation("获取分类列表")
    @WebLog(description = "获取分类列表")
    @RequestMapping(value = "/category/list", method = RequestMethod.POST)
    public Response<List<CategoryVO>> listCategory(@RequestBody CategoryQueryReq req) {
        return Response.ok(service.listCategory(req));
    }

    @ApiOperation("获取商品列表")
    @WebLog(description = "获取商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<ProductVO>> listProduct(@RequestBody ProductQueryReq req) {
        return Response.ok(service.listProduct(req));
    }

    @ApiOperation("获取商品详情")
    @WebLog(description = "获取商品详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<ProductDetailVO> detailProduct(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detailProduct(req.getId()));
    }

    @ApiOperation("重新询价-新开订单并复制旧订单数据")
    @WebLog(description = "重新询价-新开订单并复制旧订单数据")
    @RequestMapping(value = "/detail/copy_from_order", method = RequestMethod.POST)
    public Response<ProductDetailVO> detailProductCopyFormOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.copyOrder(req.getId(), LoginUserContext.getUser().getCompanyId()));
    }

    @ApiOperation("获取数保分类列表")
    @WebLog(description = "获取数保分类列表")
    @RequestMapping(value = "/di/category/list", method = RequestMethod.POST)
    public Response<List<CategoryVO>> fixListDiCategory(@RequestBody CategoryQueryReq req) {
        return Response.ok(service.listDiCategory(req));
    }

    @ApiOperation("获取数保列表")
    @WebLog(description = "获取数保分类列表")
    @RequestMapping(value = "/di/fix/category/list", method = RequestMethod.POST)
    public Response<List<CategoryVO>> listDiCategory(@RequestBody CategoryQueryReq req) {
        //req.setCategoryName("苹果");
        return Response.ok(service.listDiCategory(req));
    }

    @ApiOperation("获取数保商品列表")
    @WebLog(description = "获取数保商品列表")
    @RequestMapping(value = "/di/list", method = RequestMethod.POST)
    public Response<List<ProductVO>> listDiProduct(@RequestBody ProductQueryReq req) {
        return Response.ok(service.listDiProduct(req));
    }

    @ApiOperation("获取数保商品规格列表")
    @WebLog(description = "获取数保商品规格列表")
    @RequestMapping(value = "/di/sku/list", method = RequestMethod.POST)
    public Response<List<ProductSkuVO>> listDiProductSku(@RequestBody IdQueryReq req) {
        // 根据商品ID获取规格列表
        return Response.ok(service.listDiProductSku(req.getId()));
    }

    @ApiOperation("获取租机分类列表")
    @WebLog(description = "获取租机分类列表")
    @RequestMapping(value = "/rental/category/list", method = RequestMethod.POST)
    public Response<List<CategoryVO>> listRentalCategory(@RequestBody CategoryQueryReq req) {
        return Response.ok(service.listRentalCategory(req));
    }

    @ApiOperation("获取租机商品列表")
    @WebLog(description = "获取租机商品列表")
    @RequestMapping(value = "/rental/list", method = RequestMethod.POST)
    public Response<List<ProductVO>> listRentalProduct(@RequestBody ProductQueryReq req) {
        return Response.ok(service.listRentalProduct(req));
    }

    @ApiOperation("获取租机商品规格列表")
    @WebLog(description = "获取租机商品规格列表")
    @RequestMapping(value = "/rental/sku/list", method = RequestMethod.POST)
    public Response<List<ProductSkuVO>> listRentalProductSku(@RequestBody IdQueryReq req) {
        // 根据商品ID获取规格列表
        return Response.ok(service.listRentalProductSku(req.getId()));
    }
}