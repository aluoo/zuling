package com.anyi.common.service;

import com.anyi.common.product.domain.dto.ProductDTO;
import com.anyi.common.product.service.ProductService;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/30
 * @Copyright
 * @Version 1.0
 */
public class ProductServiceTest extends SparrowApplicationTest {
    @Autowired
    private ProductService service;

    @Test
    public void test() {
        service.lambdaQuery().last("limit 20").list();
    }

    @Test
    public void listProductTest() {
        List<ProductDTO> list = service.getBaseMapper().listProduct(Arrays.asList(1201655354553614336L, 1201655355367309313L));
        list.forEach(o -> System.out.println(o.toString()));
    }
}