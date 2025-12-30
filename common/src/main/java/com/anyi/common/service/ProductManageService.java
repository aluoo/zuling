package com.anyi.common.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.mbr.domain.MbrRentalProductSku;
import com.anyi.common.mbr.service.MbrRentalProductSkuService;
import com.anyi.common.product.domain.*;
import com.anyi.common.product.domain.constants.TreeConstants;
import com.anyi.common.product.domain.request.CategoryQueryReq;
import com.anyi.common.product.domain.request.ProductQueryReq;
import com.anyi.common.product.domain.response.CategoryVO;
import com.anyi.common.product.domain.response.ProductDetailVO;
import com.anyi.common.product.domain.response.ProductSkuVO;
import com.anyi.common.product.domain.response.ProductVO;
import com.anyi.common.product.service.*;
import com.anyi.common.util.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/31
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductManageService {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private ProductOptionService productOptionService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderOptionService orderOptionService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private MbrRentalProductSkuService rentalProductSkuService;

    public List<ProductSkuVO> listRentalProductSku(Long productId) {
        List<MbrRentalProductSku> list = rentalProductSkuService.lambdaQuery()
                .eq(MbrRentalProductSku::getProductId, productId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(MbrRentalProductSku::getSort)
                .list();
        List<ProductSkuVO> vos = BeanUtil.copyToList(list, ProductSkuVO.class);
        vos.forEach(vo -> {
            vo.setRetailPriceStr(MoneyUtil.fenToYuan(vo.getRetailPrice()));
        });
        return vos;
    }

    public List<ProductSkuVO> listDiProductSku(Long productId) {
        List<ProductSku> list = productSkuService.lambdaQuery()
                .eq(ProductSku::getProductId, productId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(ProductSku::getSort)
                .list();
        List<ProductSkuVO> vos = BeanUtil.copyToList(list, ProductSkuVO.class);
        vos.forEach(vo -> {
            vo.setRetailPriceStr(MoneyUtil.fenToYuan(vo.getRetailPrice()));
        });
        return vos;
    }

    public List<CategoryVO> listCategory(CategoryQueryReq req) {
        if (req.getParentId() == null) {
            req.setParentId(TreeConstants.TOP_PARENT);
        }
        List<Category> list = categoryService.listCategoriesByParentId(req.getParentId());
        return BeanUtil.copyToList(list, CategoryVO.class);
    }

    public List<CategoryVO> listDiCategory(CategoryQueryReq req) {
        if (req.getParentId() == null) {
            req.setParentId(TreeConstants.TOP_PARENT);
        }
        // 只列出售卖数保的分类
        Set<Long> diAbleCategoryIds = productService.lambdaQuery()
                .select(Product::getCategoryId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Product::getDigitalInsuranceAble, true)
                .list().stream().map(Product::getCategoryId).collect(Collectors.toSet());
        Set<Long> diAbleCategoryParentIds = categoryService.lambdaQuery()
                .select(Category::getParentId)
                .in(Category::getId, diAbleCategoryIds)
                .list().stream().map(Category::getParentId).collect(Collectors.toSet());
        Set<Long> includeIds = Stream.concat(diAbleCategoryIds.stream(), diAbleCategoryParentIds.stream()).collect(Collectors.toSet());

        List<Category> list = categoryService.lambdaQuery()
                .eq(Category::getParentId, req.getParentId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(StrUtil.isNotBlank(req.getCategoryName()),Category::getName,req.getCategoryName())
                .in(CollUtil.isNotEmpty(includeIds), Category::getId, includeIds)
                .orderByAsc(Category::getLevel)
                .orderByAsc(Category::getSort)
                .list();

        return BeanUtil.copyToList(list, CategoryVO.class);
    }

    public List<CategoryVO> listRentalCategory(CategoryQueryReq req) {
        if (req.getParentId() == null) {
            req.setParentId(TreeConstants.TOP_PARENT);
        }
        // 只列出租机的分类
        Set<Long> mobileRentalAbleCategoryIds = productService.lambdaQuery()
                .select(Product::getCategoryId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Product::getMobileRentalAble, true)
                .list().stream().map(Product::getCategoryId).collect(Collectors.toSet());
        Set<Long> mobileRentalAbleCategoryParentIds = categoryService.lambdaQuery()
                .select(Category::getParentId)
                .in(Category::getId, mobileRentalAbleCategoryIds)
                .list().stream().map(Category::getParentId).collect(Collectors.toSet());
        Set<Long> includeIds = Stream.concat(mobileRentalAbleCategoryIds.stream(), mobileRentalAbleCategoryParentIds.stream()).collect(Collectors.toSet());

        List<Category> list = categoryService.lambdaQuery()
                .eq(Category::getParentId, req.getParentId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(StrUtil.isNotBlank(req.getCategoryName()),Category::getName,req.getCategoryName())
                .in(CollUtil.isNotEmpty(includeIds), Category::getId, includeIds)
                .orderByAsc(Category::getLevel)
                .orderByAsc(Category::getSort)
                .list();

        return BeanUtil.copyToList(list, CategoryVO.class);
    }

    public List<ProductVO> listProduct(ProductQueryReq req) {

        List<Product> list;
        if (StrUtil.isNotBlank(req.getKeyword())) {
            list = productService.lambdaQuery()
                    .like(Product::getName, req.getKeyword())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Product::getActivated, true)
                    .orderByAsc(Product::getSort)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        } else {
            if (req.getParentId() == null) {
                throw new BusinessException(-1, "一级分类ID不能为空");
            }
            Long parentId = req.getParentId();
            Long categoryId = req.getCategoryId();
            List<Long> targetCategoryIds = Optional.ofNullable(categoryId)
                    .map(Collections::singletonList)
                    .orElseGet(() -> categoryService.listCategoriesByParentId(parentId).stream().map(Category::getId).collect(Collectors.toList()));
            list = productService.lambdaQuery()
                    .in(Product::getCategoryId, targetCategoryIds)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Product::getActivated, true)
                    .orderByAsc(Product::getSort)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        }
        return buildProductVo(list);
        /*List<ProductVO> vo = BeanUtil.copyToList(list, ProductVO.class);
        if (CollUtil.isNotEmpty(vo)) {
            List<Long> categoryIds = vo.stream().map(ProductVO::getCategoryId).collect(Collectors.toList());
            List<Category> categories
                    = categoryService.lambdaQuery()
                    .in(Category::getId, categoryIds)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .list();
            Map<Long, Category> categoryMap = categoryService.buildCategoryMap(categories);
            vo.forEach(o -> o.setCategoryName(Optional.ofNullable(categoryMap.get(o.getCategoryId())).map(Category::getName).orElse(null)));
        }
        return vo;*/
    }

    public List<ProductVO> listDiProduct(ProductQueryReq req) {
        List<Product> list;
        if (StrUtil.isNotBlank(req.getKeyword())) {
            list = productService.lambdaQuery()
                    .like(Product::getName, req.getKeyword())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Product::getActivated, true)
                    .eq(Product::getDigitalInsuranceAble, true)
                    .orderByAsc(Product::getSort)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        } else {
            if (req.getParentId() == null) {
                throw new BusinessException(-1, "一级分类ID不能为空");
            }
            Long parentId = req.getParentId();
            Long categoryId = req.getCategoryId();
            List<Long> targetCategoryIds = Optional.ofNullable(categoryId)
                    .map(Collections::singletonList)
                    .orElseGet(() -> categoryService.listCategoriesByParentId(parentId).stream().map(Category::getId).collect(Collectors.toList()));
            list = productService.lambdaQuery()
                    .in(Product::getCategoryId, targetCategoryIds)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Product::getActivated, true)
                    .eq(Product::getDigitalInsuranceAble, true)
                    .orderByAsc(Product::getSort)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        }
        return buildProductVo(list);
    }

    public List<ProductVO> listRentalProduct(ProductQueryReq req) {
        List<Product> list;
        if (StrUtil.isNotBlank(req.getKeyword())) {
            list = productService.lambdaQuery()
                    .like(Product::getName, req.getKeyword())
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Product::getActivated, true)
                    .eq(Product::getMobileRentalAble, true)
                    .orderByAsc(Product::getSort)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        } else {
            if (req.getParentId() == null) {
                throw new BusinessException(-1, "一级分类ID不能为空");
            }
            Long parentId = req.getParentId();
            Long categoryId = req.getCategoryId();
            List<Long> targetCategoryIds = Optional.ofNullable(categoryId)
                    .map(Collections::singletonList)
                    .orElseGet(() -> categoryService.listCategoriesByParentId(parentId).stream().map(Category::getId).collect(Collectors.toList()));
            list = productService.lambdaQuery()
                    .in(Product::getCategoryId, targetCategoryIds)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Product::getActivated, true)
                    .eq(Product::getMobileRentalAble, true)
                    .orderByAsc(Product::getSort)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        }
        return buildProductVo(list);
    }

    private List<ProductVO> buildProductVo(List<Product> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<ProductVO> vos = BeanUtil.copyToList(list, ProductVO.class);
        if (CollUtil.isNotEmpty(vos)) {
            List<Long> categoryIds = vos.stream().map(ProductVO::getCategoryId).collect(Collectors.toList());
            List<Category> categories
                    = categoryService.lambdaQuery()
                    .in(Category::getId, categoryIds)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .list();
            Map<Long, Category> categoryMap = categoryService.buildCategoryMap(categories);
            vos.forEach(o -> o.setCategoryName(Optional.ofNullable(categoryMap.get(o.getCategoryId())).map(Category::getName).orElse(null)));
        }
        return vos;
    }

    public ProductDetailVO detailProduct(Long productId) {
        Product bean = productService.lambdaQuery()
                .eq(Product::getId, productId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Product::getActivated, true)
                .one();
        if (bean == null) {
            throw new BusinessException(-1, "没有找到产品信息");
        }
        ProductDetailVO vo = ProductDetailVO.builder()
                .id(bean.getId())
                .productId(bean.getId())
                .name(bean.getName())
                .productName(bean.getName())
                .build();
        // build category info
        List<CategoryVO> categories = buildCategoryInfo(bean.getCategoryId());
        vo.setCategories(categories);

        // build option info by relation
        List<Tree<Long>> optionals = optionService.buildOptionTreeBaseByProductId(productId);
        List<Tree<Long>> filterOptionals = new ArrayList<>();
        if (CollUtil.isNotEmpty(optionals)) {
            // 防止后台误操作将一个手机关联多个顶级选项配置，只取第一个
            filterOptionals.add(optionals.get(0));
        }
        vo.setOptionals(filterOptionals);

        // build remark info
        vo.setRemarks(dictService.getRemarkInfo());

        return vo;
    }

    public ProductDetailVO copyOrder(Long orderId, Long companyId) {
        Order order = Optional.ofNullable(orderService.lambdaQuery()
                        .eq(Order::getId, orderId)
                        .eq(Order::getStoreCompanyId, companyId)
                        .one()).orElseThrow(() -> new BaseException(BizError.MB_ORDER_NOT_EXISTS));

        Product bean = Optional.ofNullable(productService.lambdaQuery()
                .eq(Product::getId, order.getProductId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Product::getActivated, true)
                .one()).orElseThrow(() -> new BusinessException(-1, "没有找到产品信息"));

        ProductDetailVO vo = ProductDetailVO.builder()
                .id(bean.getId())
                .productId(bean.getId())
                .name(bean.getName())
                .productName(bean.getName())
                .build();
        // build category info
        List<CategoryVO> categories = buildCategoryInfo(bean.getCategoryId());
        vo.setCategories(categories);

        List<OrderOption> allOptionsByOrderId = orderOptionService.getAllOptionsByOrderId(orderId);
        // build option info by relation
        List<Tree<Long>> optionals = optionService.buildOptionTreeBaseByProductIdFillValueFromExistOrder(order.getProductId(), allOptionsByOrderId);
        vo.setOptionals(CollUtil.isNotEmpty(optionals) ? optionals : new ArrayList<>());

        // build remark info
        vo.setRemarks(dictService.getRemarkInfo());

        vo.setExistRemarks(order.getRemark());
        return vo;
    }

    private List<CategoryVO> buildCategoryInfo(Long leafCategoryId) {
        if (leafCategoryId == null) {
            return new ArrayList<>();
        }
        Category leafCategory = categoryService.lambdaQuery()
                .select(Category::getId, Category::getAncestors)
                .eq(Category::getId, leafCategoryId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        if (leafCategory == null) {
            return new ArrayList<>();
        }
        List<String> split = StrUtil.split(leafCategory.getAncestors(), TreeConstants.SPLIT_CHAR);
        if (CollUtil.isEmpty(split)) {
            return new ArrayList<>();
        }
        List<Category> list = categoryService.lambdaQuery()
                .in(Category::getId, split)
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(Category::getLevel)
                .list();
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(list, CategoryVO.class);
    }
}