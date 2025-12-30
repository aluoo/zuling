package com.anyi.common.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.dto.OrderShippingCountDTO;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.request.OrderQueryReq;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.service.OrderService;
import com.anyi.sparrow.SparrowApplicationTest;
import com.anyi.sparrow.product.service.OrderManageService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/1
 * @Copyright
 * @Version 1.0
 */
public class OrderServiceTest extends SparrowApplicationTest {
    @Autowired
    OrderService service;
    @Autowired
    OrderManageService orderManageService;

    @Test
    public void test() {
        service.lambdaQuery().last("limit 10").list();
        service.getBaseMapper().selectById(-1L);
    }

    @Test
    public void testKeywordSearch() {
        String keyword = "30625";
        List<Order> list = service.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .and(StrUtil.isNotBlank(keyword), wp -> wp.eq(Order::getId, Long.valueOf(keyword)).or().eq(Order::getOrderNo, keyword))
                .ge(Order::getCreateTime, "2021-01-01 00:00:00")
                .le(Order::getCreateTime, "2024-12-31 23:59:59")
                .list();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testOrderList() {
        OrderQueryReq req = OrderQueryReq.builder()
                .page(3)
                .pageSize(10)
                .build();
        PageInfo<OrderDetailVO> list = orderManageService.listOrder(req);
        list.getList().forEach(o -> System.out.println(o.toString()));
    }

    @Test
    public void conditionCountOrderGroupByRecyclerTest() {
        OrderShippingCountDTO dto = OrderShippingCountDTO.builder()
                .storeCompanyId(1749717531993853954L)
                .recyclerCompanyIds(Arrays.asList(1764988731541995521L))
                .status(OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .now(new Date())
                .countType(1)
                .build();
        service.getBaseMapper().conditionCountOrderGroupByRecycler(dto);
        OrderShippingCountDTO dto2 = OrderShippingCountDTO.builder()
                .storeCompanyId(1749717531993853954L)
                .recyclerCompanyIds(Arrays.asList(1764988731541995521L))
                .status(OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .now(new Date())
                .countType(2)
                .build();
        service.getBaseMapper().conditionCountOrderGroupByRecycler(dto2);
        OrderShippingCountDTO dto3 = OrderShippingCountDTO.builder()
                .storeCompanyId(1749717531993853954L)
                .recyclerCompanyIds(Arrays.asList(1764988731541995521L))
                .status(OrderStatusEnum.PENDING_RECEIPT.getCode())
                .now(new Date())
                .build();
        service.getBaseMapper().conditionCountOrderGroupByRecycler(dto3);
    }
}