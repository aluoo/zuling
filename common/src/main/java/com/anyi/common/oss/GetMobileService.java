package com.anyi.common.oss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.GetMobileRequest;
import com.aliyun.dypnsapi20170525.models.GetMobileResponse;
import com.aliyun.dypnsapi20170525.models.GetMobileResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.anyi.common.advice.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class GetMobileService {
    @Value("${ali.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${ali.oss.accessKeySecret:}")
    private String accessKeySecret;

    private Client client;

    @PostConstruct
    private Client initClient() throws Exception {
        Config config = new Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(accessKeyId)
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "dypnsapi.aliyuncs.com";
        return this.client = new Client(config);
    }

    public GetMobileResponseBody getMobile(String accessToken) {
        GetMobileRequest getMobileRequest = new GetMobileRequest();
        getMobileRequest.setAccessToken(accessToken);
        try {
            GetMobileResponse response = client.getMobile(getMobileRequest);
            String code = response.body.code;
            if (!com.aliyun.teautil.Common.equalString(code, "OK")) {
                log.error("aliYun.getMobile.error: {}", response.body.message);
                return null;
            }

            log.info("aliYun.getMobile.res: {}", JSONUtil.toJsonStr(response.body));
            return response.body;
        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            log.error("aliYun.getMobile.error: {}", error.getMessage());
            // 诊断地址
            log.error("aliYun.getMobile.error.recommend: {}", error.getData().get("Recommend"));
            throw new BusinessException(-1, StrUtil.format("GetMobileError-{}", error.message));
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            log.error("aliYun.getMobile.error: {}", error.getMessage());
            // 诊断地址
            log.error("aliYun.getMobile.error.recommend: {}", error.getData().get("Recommend"));
            throw new BusinessException(-1, StrUtil.format("GetMobileError-{}", error.message));
        }
    }
}