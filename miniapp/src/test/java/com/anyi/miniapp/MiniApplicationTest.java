package com.anyi.miniapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MiniApplication.class)
public class MiniApplicationTest {

    @Test
    public void contextLoads() {
    }
}