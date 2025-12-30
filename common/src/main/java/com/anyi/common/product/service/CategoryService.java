package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.Category;
import com.anyi.common.product.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    public List<Category> listCategoriesByParentId(Long parentId) {
        return this.lambdaQuery()
                .eq(Category::getParentId, parentId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(Category::getLevel)
                .orderByAsc(Category::getSort)
                .list();
    }

    public Map<Long, Category> buildCategoryMap(Collection<Category> list) {
        return CollUtil.isEmpty(list)
                ? new HashMap<>(1)
                : list.stream().collect(Collectors.toMap(Category::getId, Function.identity(), (oldValue, newValue) -> oldValue));
    }
}