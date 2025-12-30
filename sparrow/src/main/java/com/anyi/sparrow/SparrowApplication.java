package com.anyi.sparrow;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.anyi.sparrow.wechat.config.WxMiniProperties;
import com.anyi.sparrow.wechat.config.WxMpProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@EnableSwagger2
@EnableSwaggerBootstrapUI
@SpringBootApplication(scanBasePackages = {"com.anyi"})
@EnableTransactionManagement
@EnableConfigurationProperties(value = {WxMpProperties.class, WxMiniProperties.class})
@MapperScan(basePackages = {"com.anyi.**.mapper"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableRetry
public class SparrowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparrowApplication.class, args);

        System.out.println(
                "######  ########     ###    ########  ########   #######  ##      ## \n" +
                        "##    ## ##     ##   ## ##   ##     ## ##     ## ##     ## ##  ##  ## \n" +
                        "##       ##     ##  ##   ##  ##     ## ##     ## ##     ## ##  ##  ## \n" +
                        " ######  ########  ##     ## ########  ########  ##     ## ##  ##  ## \n" +
                        "      ## ##        ######### ##   ##   ##   ##   ##     ## ##  ##  ## \n" +
                        "##    ## ##        ##     ## ##    ##  ##    ##  ##     ## ##  ##  ## \n" +
                        " ######  ##        ##     ## ##     ## ##     ##  #######   ###  ###  "
        );

        System.out.println("Sparrow启动成功!");
    }
}
