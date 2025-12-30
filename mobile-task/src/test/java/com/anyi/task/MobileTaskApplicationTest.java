package com.anyi.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MobileTaskApplication.class)
public class MobileTaskApplicationTest {
    @Test
    public void contextLoads() {
    }
}