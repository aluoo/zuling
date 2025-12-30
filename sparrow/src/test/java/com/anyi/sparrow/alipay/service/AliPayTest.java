
package com.anyi.sparrow.alipay.service;

import com.anyi.sparrow.SparrowApplication;
import com.anyi.sparrow.assist.projects.dao.mapper.SysProjectsMapper;
import com.anyi.sparrow.common.vo.WxOpenId;
import com.anyi.sparrow.wechat.service.TemplateMsgService;
import com.anyi.sparrow.wechat.service.WechatMiniHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SparrowApplication.class})
public class AliPayTest {
    @Autowired
    AliPayService aliPayService;

    @Test
    public void getOpenId() {
        aliPayService.getOpenId("4b203fe6c11548bcabd8da5bb087a83b");
    }

    @Test
    public void transfer() {
        aliPayService.transfer("20240129180000", "", "0.2");
    }

    @Test
    public void query() {
        aliPayService.query("20240129180000");
    }

}
