package com.anyi.common.service;

import com.anyi.common.product.domain.Category;
import com.anyi.common.product.service.CategoryService;
import com.anyi.sparrow.SparrowApplicationTest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/29
 * @Copyright
 * @Version 1.0
 */
public class CategoryServiceTest extends SparrowApplicationTest {
    @Autowired
    private CategoryService service;

    @Test
    public void test() {
        service.list();
        service.getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

    @Test
    @Transactional
    public void test2() {
        Category bean = Category.builder()
                .id(123L)
                .name("123-test")
                .build();
        service.save(bean);

        service.lambdaUpdate()
                .set(Category::getName, "test-123")
                .eq(Category::getId, 123L)
                .update(new Category());

    }
}