package com.anyi.task.task;

import com.anyi.task.MobileTaskApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/12/12
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class OrderTaskTest extends MobileTaskApplicationTest {
    @Autowired
    private OrderTask task;

    @Test
    public void autoCloseOverdueOrderQuoteTest() {
        // 超时关闭报价功能
        task.autoCloseOverdueOrderQuote();
    }

    @Test
    public void autoCloseOverdueOrder() {
        // 超时关闭订单
        task.autoCloseOverdueOrder();
    }
}