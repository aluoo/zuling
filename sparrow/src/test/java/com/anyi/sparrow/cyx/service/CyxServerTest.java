
package com.anyi.sparrow.cyx.service;

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
public class CyxServerTest {
    @Autowired
    WechatMiniHandler wechatMiniHandler;
    @Autowired
    SysProjectsMapper sysProjectsMapper;
    @Autowired
    TemplateMsgService templateMsgService;

    @Test
    public void vehicleUniqueCheckTest() {
        /*List<Long> ids = companyMapper.queryAutoSettleCompanyIds();
        String aa = "21312312";*/
    }

    @Test
    public void sysproject() {
        WxOpenId openId = wechatMiniHandler.getOpenId("0e1ZS00w3Bfi823qzX1w3VEnO43ZS00q");
        System.out.println("openId是" + openId.getOpenId());
    }

    @Test
    public void message() {
        templateMsgService.pushOrderMsg("", "", "", "", "", "");
        String aa = "21312312";
    }


}