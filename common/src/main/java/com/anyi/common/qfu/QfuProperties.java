package com.anyi.common.qfu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/21
 * @Copyright
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "qfu")
public class QfuProperties {

    private String vendorId;
    private String merchantId;
    private String agencyId;
    private String clientKeyPath;
    private String serverKeyPath;
}