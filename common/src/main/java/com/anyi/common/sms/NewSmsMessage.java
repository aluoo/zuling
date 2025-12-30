package com.anyi.common.sms;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSmsMessage {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 模板编号
     */
    private String tplId;
    /**
     * 短信参数
     */
    private Map<String, String> tplValue = new HashMap<>();


    public void setCode(String code) {
        tplValue.put("#code#", code);
    }

    public void setCarCode(String carCode) {
        tplValue.put("#car_code#", carCode);
    }

    public String getTplValue() {
        StringBuilder builder = new StringBuilder();
        tplValue.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
        String value = builder.toString();
        int last = value.lastIndexOf("&");
        return value.substring(0, last);
    }

    public void addParam(String param,String value){
        tplValue.put(param,value);
    }


    public Map<String, String> getParam(String key) {
        Map<String, String> param = new HashMap<>();
        param.put("mobile", this.getMobile());
        param.put("tpl_id", this.getTplId());
        param.put("tpl_value", this.getTplValue());
        param.put("key", key);
        return param;
    }




}
