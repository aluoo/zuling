package com.anyi.common.service;

import cn.hutool.json.JSONUtil;
import com.anyi.common.product.domain.OrderLog;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/26
 * @Copyright
 * @Version 1.0
 */
public class OrderLogServiceTest extends SparrowApplicationTest {
    @Autowired
    private OrderLogService service;

    @Test
    @Transactional
    public void test() {
        service.addLog(null, 1L, 1, 1, "1", "1");
        OrderLog byId = service.lambdaQuery().eq(OrderLog::getOrderId, 1L).one();
        System.out.println(JSONUtil.toJsonStr(byId));
    }
}