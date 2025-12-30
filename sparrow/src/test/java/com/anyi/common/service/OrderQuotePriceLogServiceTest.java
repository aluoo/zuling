package com.anyi.common.service;

import com.anyi.common.product.domain.dto.OrderQuoteInfoDTO;
import com.anyi.common.product.domain.dto.OrderQuotePriceLogCountDTO;
import com.anyi.common.product.domain.request.OrderQuoteQueryReq;
import com.anyi.common.product.service.OrderQuotePriceLogService;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/1
 * @Copyright
 * @Version 1.0
 */
public class OrderQuotePriceLogServiceTest extends SparrowApplicationTest {
    @Autowired
    OrderQuotePriceLogService service;

    @Test
    public void test() {
        service.lambdaQuery().last("limit 10").list();
        service.getBaseMapper().selectById(-1L);
    }

    @Test
    public void countGroupByOrderIdsTest() {
        List<OrderQuotePriceLogCountDTO> list = service.getBaseMapper().countGroupByOrderIds(Arrays.asList(1l, 2l, 3l));
        list.forEach(o -> System.out.println(o.toString()));
    }

    @Test
    public void listQuoteInfoByOrderIdTest() {
        List<OrderQuoteInfoDTO> list = service.getBaseMapper().listQuoteInfoByOrderId(OrderQuoteQueryReq.builder().orderId(1213489969784557569L).build());
        list.forEach(o -> System.out.println(o.toString()));
    }
}