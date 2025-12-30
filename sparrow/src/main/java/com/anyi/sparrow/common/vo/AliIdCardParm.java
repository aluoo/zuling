package com.anyi.sparrow.common.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ali.idcard")
@Component
@Data
public class AliIdCardParm {

    private String accountId;

    private String verifyKey;

    private String appCode;
}
