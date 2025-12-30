package com.anyi.sparrow.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.common.vo.AliIdCardParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IdCardCheck {
    @Autowired
    private AliIdCardParm aliIdCardParm;

    public void check(String idCard, String name){
        String host = "https://safrvcert.market.alicloudapi.com";
        String path = "/safrv_2meta_id_name/";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + aliIdCardParm.getAppCode());
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("__userId", aliIdCardParm.getAccountId());
        querys.put("customerID", "1232233252");
        querys.put("identifyNum", idCard);
        querys.put("userName", name);
        querys.put("verifyKey", aliIdCardParm.getVerifyKey());
        String s = HttpClientUtil.sendGet(host + path, headers, querys);
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (jsonObject.getInteger("code") == 200 && jsonObject.getJSONObject("value").getInteger("bizCode") == 0){
            return;
        }
        throw new BusinessException(BizError.IDCARD_ERROR);
    }

    public static void main(String[] args) {
        IdCardCheck idCardCheck = new IdCardCheck();
        AliIdCardParm parm = new AliIdCardParm();
        parm.setAccountId("1560559117052501");
        parm.setAppCode("5b18c17950d24f778f14c74eead16ae5");
        parm.setVerifyKey("IVg9slwIy71sQ3");
        idCardCheck.aliIdCardParm = parm;
        idCardCheck.check("612401198609240953", "王莹");
    }
}