package com.anyi.commission.service;

import com.anyi.sparrow.SparrowApplication;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 * @since 2023/9/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {SparrowApplication.class})
@Slf4j
public class MenuServiceTest {

    @Autowired
    private WxMpService wxMpService;

    @Test
    public void menuQuery() throws WxErrorException {

        WxMpGetSelfMenuInfoResult result =  wxMpService.getMenuService().getSelfMenuInfo();
        System.out.println(result.toString());

    }

    @Test
    public void menuCreate() throws WxErrorException {

        List<WxMenuButton> buttons = new ArrayList<>();

        List<WxMenuButton> oneButtons = new ArrayList<>();

        WxMenuButton oneButtonOne = new WxMenuButton();
        oneButtonOne.setType("view");
        oneButtonOne.setName("下载机享转APP");
        oneButtonOne.setKey("down_app_btn");
        oneButtonOne.setUrl("http://anyichuxing.com/dl/jixiangzhuan/");
        oneButtons.add(oneButtonOne);

        WxMenuButton twoButtonTwo = new WxMenuButton();
        twoButtonTwo.setType("view");
        twoButtonTwo.setName("下载云联交通APP");
        twoButtonTwo.setKey("down_yljt_btn");
        twoButtonTwo.setUrl("https://www.anyichuxing.com/dl/yunlian/index.html");
        oneButtons.add(twoButtonTwo);

        WxMenuButton button1 = new WxMenuButton();
        button1.setName("下载APP");
        button1.setSubButtons(oneButtons);
        buttons.add(button1);

        /*WxMenuButton button = new WxMenuButton();
        button.setType("view");
        button.setName("下载APP");
        button.setKey("down_app_btn");
        button.setUrl("http://anyichuxing.com/dl/jixiangzhuan/");
        buttons.add(button);*/

        List<WxMenuButton> bjButtons = new ArrayList<>();

        WxMenuButton bjOne = new WxMenuButton();
        bjOne.setType("view");
        bjOne.setName("OPPO");
        bjOne.setKey("insurance_oppo");
        bjOne.setUrl("https://support.oppo.com/cn/spare-parts-price/");
        bjButtons.add(bjOne);

        WxMenuButton bjTwo = new WxMenuButton();
        bjTwo.setType("view");
        bjTwo.setName("VIVO");
        bjTwo.setKey("insurance_vivo");
        bjTwo.setUrl("http://www.vivo.com.cn/service/accessory");
        bjButtons.add(bjTwo);

        WxMenuButton bjThree = new WxMenuButton();
        bjThree.setType("view");
        bjThree.setName("华为");
        bjThree.setKey("insurance_huawei");
        bjThree.setUrl("https://consumer.huawei.com/cn/support/sparepart-price/");
        bjButtons.add(bjThree);

        WxMenuButton bjFour = new WxMenuButton();
        bjFour.setType("view");
        bjFour.setName("小米");
        bjFour.setKey("insurance_xiaomi");
        bjFour.setUrl("https://www.mi.com/service/materialprice");
        bjButtons.add(bjFour);

        WxMenuButton bjFIVE = new WxMenuButton();
        bjFIVE.setType("view");
        bjFIVE.setName("荣耀");
        bjFIVE.setKey("insurance_honor");
        bjFIVE.setUrl("https://www.hihonor.com/cn/support/sparepart-price/");
        bjButtons.add(bjFIVE);

        /*WxMenuButton subButton3 = new WxMenuButton();
        subButton3.setType("view");
        subButton3.setName("个人中心");
        subButton3.setKey("insurance_center");
        subButton3.setUrl("http://anyichuxing.com/dl/jixiangzhuan/");
        subButtons.add(subButton3);*/


        WxMenuButton button3 = new WxMenuButton();
        button3.setName("备件查询");
        button3.setSubButtons(bjButtons);
        buttons.add(button3);

        List<WxMenuButton> subButtons = new ArrayList<>();

        WxMenuButton subButtonOne = new WxMenuButton();
        subButtonOne.setType("view");
        subButtonOne.setName("保单查询");
        subButtonOne.setKey("insurance_query");
        subButtonOne.setUrl("http://anyichuxing.com/dl/jixiangzhuan/");
        subButtons.add(subButtonOne);

        WxMenuButton subButtonTwo = new WxMenuButton();
        subButtonTwo.setType("view");
        subButtonTwo.setName("我要报修");
        subButtonTwo.setKey("insurance_fix");
        subButtonTwo.setUrl("http://anyichuxing.com/dl/jixiangzhuan/");
        subButtons.add(subButtonTwo);


        WxMenuButton button2 = new WxMenuButton();
        button2.setName("数保服务");
        button2.setSubButtons(subButtons);
        buttons.add(button2);


        WxMenu wxMenu = new WxMenu();
        wxMenu.setButtons(buttons);
        System.out.println(wxMenu.toJson());
        String result =  wxMpService.getMenuService().menuCreate(wxMenu);
        System.out.println(result);

    }


}
