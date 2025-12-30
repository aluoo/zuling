package com.anyi.common.product.mapper;

import cn.hutool.json.JSONUtil;
import com.anyi.common.product.domain.Option;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/31
 * @Copyright
 * @Version 1.0
 */
public class OptionMapperTest extends SparrowApplicationTest {
    @Autowired
    private OptionMapper dao;

    @Test
    public void test() {
        List<Option> options = dao.listOptionsByProductId(1201655354553614336L);
        System.out.println(JSONUtil.toJsonStr(options));
    }
}