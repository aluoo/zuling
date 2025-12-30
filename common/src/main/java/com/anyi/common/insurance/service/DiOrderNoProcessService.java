package com.anyi.common.insurance.service;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.domain.DiInsuranceFixOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/19
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class DiOrderNoProcessService {
    @Autowired
    private DiInsuranceFixOrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String ORDER_NO_COUNT = "insurance_fix_order_no_count";

    public String nextOrderNo() {
        // 生成订单码，6位，纯数字，唯一，用redis递增
        // get and increase
        String cacheValue = getFromCache();
        String orderNo = String.format("%06d", Integer.parseInt(cacheValue));
        incrementOrderNoCount();
        return orderNo;
    }

    private void incrementOrderNoCount() {
        redisTemplate.opsForValue().increment(ORDER_NO_COUNT);
    }

    private String getFromCache() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(ORDER_NO_COUNT))) {
            rebuildCount();
        }
        return redisTemplate.opsForValue().get(ORDER_NO_COUNT);
    }

    @PostConstruct
    public void rebuildCount() {
        log.info("init order no count.start");
        if (Boolean.TRUE.equals(redisTemplate.hasKey(ORDER_NO_COUNT))) {
            log.info("init order no count.end: exist");
            return;
        }
        Integer cnt = null;
        DiInsuranceFixOrder order = orderService.lambdaQuery()
                .select(DiInsuranceFixOrder::getId, DiInsuranceFixOrder::getOrderNo)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(DiInsuranceFixOrder::getId)
                .orderByDesc(DiInsuranceFixOrder::getOrderNo)
                .last("limit 1")
                .one();
        if (order != null) {
            try {
                cnt = Integer.parseInt(order.getOrderNo());
            } catch (NumberFormatException e) {
                log.info("rebuildCount.error: {}", ExceptionUtil.getMessage(e));
            }
        }
        if (cnt == null) {
            Long count = orderService.lambdaQuery().count();
            cnt = count.intValue();
        }
        String value = String.valueOf(cnt + 1);
        redisTemplate.opsForValue().set(ORDER_NO_COUNT, value);
        log.info("init order no count.end: rebuild {}", value);
    }
}