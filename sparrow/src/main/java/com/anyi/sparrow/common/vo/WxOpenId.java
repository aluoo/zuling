package com.anyi.sparrow.common.vo;

import lombok.Data;

@Data
public class WxOpenId {
    //open id
    private String openId;

    //session Key
    private String sessionKey;

    private String unionId;
}
