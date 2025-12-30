package com.anyi.sparrow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparrowApplication.class)
public class SparrowApplicationTest {

    @Test
    public void contextLoads() {
    }
}