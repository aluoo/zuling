package com.anyi.common.sms;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class NewSmsSender {

    public static final String URL = "http://v.juhe.cn/sms/send?mobile={mobile}&tpl_id={tpl_id}&tpl_value={tpl_value}&key={key}";
    public static final String KEY = "7d41657a0605fa554965203096b6bcfe";

    @Value("${spring.profiles.active}")
    private String env;

    @Async
    public void doSend(NewSmsMessage smsMessage) {

        if("production".equals(env)){

            log.info("【发送短信】{}", JSONUtil.toJsonStr(smsMessage));
            RestTemplate restTemplate = new RestTemplate();
            Object o = restTemplate.getForEntity(URL, Object.class, smsMessage.getParam(KEY));
            log.info(JSONUtil.toJsonStr(o));
            System.out.println(o);
        }else {
            log.info("非正式环境，模拟发送短信:" + smsMessage.toString());
            return;
        }
    }


    /*public static void main(String[] args) {
        NewSmsMessage smsMessage = new NewSmsMessage();
        smsMessage.setMobile("18405025550");

        smsMessage.setTplId(NewSmsTemplate.USER_ETC_ARREARAGE.getTplId());
        smsMessage.setCarCode("闽D0990");

        //smsMessage.setTplId("252760");
        //smsMessage.setCode("7677");

        doSend(smsMessage);
    }*/

}
