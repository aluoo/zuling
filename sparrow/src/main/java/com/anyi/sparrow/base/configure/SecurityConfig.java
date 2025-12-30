package com.anyi.sparrow.base.configure;

import com.anyi.common.interceptor.RepeatSubmitInterceptor;
import com.anyi.sparrow.applet.user.service.UserTokenChecker;
import com.anyi.sparrow.base.security.CorsInterceptor;
import com.anyi.sparrow.base.security.ExchangePhoneToolInterceptor;
import com.anyi.sparrow.base.security.PathPatterns;
import com.anyi.sparrow.base.security.TokenCheckInterceptor;
import com.anyi.sparrow.common.enums.UserType;
import com.anyi.sparrow.cyx.config.ApiSignConfig;
import com.anyi.sparrow.cyx.interceptor.CheckSignInfoInterceptor;
import com.anyi.sparrow.organize.employee.service.EmployeeTokenChecker;
import com.anyi.sparrow.organize.employee.service.InsuranceEmployeeTokenChecker;
import com.anyi.sparrow.organize.employee.service.TemporaryEmployeeTokenChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Controller
public class SecurityConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private ApiSignConfig apiSignConfig;

    @Bean
    public ExchangePhoneToolInterceptor exchangePhoneToolInterceptor() {
        return new ExchangePhoneToolInterceptor();
    }

    @Bean
    public RepeatSubmitInterceptor repeatSubmitInterceptor() {
        return new RepeatSubmitInterceptor();
    }

    @Bean
    public EmployeeTokenChecker employeeTokenChecker() {
        EmployeeTokenChecker tokenChecker = new EmployeeTokenChecker();
        return tokenChecker;
    }

    @Bean
    public UserTokenChecker userTokenChecker() {
        UserTokenChecker tokenChecker = new UserTokenChecker();
        return tokenChecker;
    }

    @Bean
    public TemporaryEmployeeTokenChecker temporaryEmployeeTokenChecker() {
        return new TemporaryEmployeeTokenChecker();
    }

    @Bean
    public InsuranceEmployeeTokenChecker insuranceEmployeeTokenChecker() {
        return new InsuranceEmployeeTokenChecker();
    }

    /**
     * 员工token检查
     * @param registry
     */
    private void addTokenInterceptor(InterceptorRegistry registry) {
        TokenCheckInterceptor interceptor = new TokenCheckInterceptor(redisTemplate);
        interceptor.addTokenChecker(UserType.EMPLOYEE.getCode(), employeeTokenChecker());
        interceptor.addTokenChecker(UserType.USER.getCode(), userTokenChecker());
        interceptor.addTokenChecker(UserType.TEMPORARY_EMPLOYEE.getCode(), temporaryEmployeeTokenChecker());
        interceptor.addTokenChecker(UserType.INSURANCE_EMPLOYEE.getCode(), insuranceEmployeeTokenChecker());
        InterceptorRegistration registration = registry.addInterceptor(interceptor);
        registration.addPathPatterns(PathPatterns.create().add("/**").toArray());
        PathPatterns patterns = PathPatterns.createDefault()
                .add("/temp/cyx/**")
                .add("/api/cyx/**")
                .add("/snowflakeIdConfig/**")
                .add("/sendIssueRecord")
                .add("/wxMenuPush")
                .add("/**/login")
                .add("/**/rvt/create")
                .add("/**/getVerifyCode")
                .add("/**/getOpenId")
                .add("/**/wx-login")
                .add("/wechat/mp/**")
                .add("/best-sign/notify")
                .add("/**/pay-callback")
                .add("/**/health-check")
                .add("/ws/v1.0/cloud/pay/callback")
                .add("/wx/pay/v1.0/refund-callback")
                .add("/wx/pay/v1.0/refund-test")
                .add("/wx/pay/v1.0/pay-callback/temporary")
                .add("/wx/mini/getAccessToken")
                .add("/wx/gzh/getAccessToken")
                .add("/**/getUpgradeInfo")
                .add("/open/**")
                .add("/health.html")
                .add("/**/getLoginSmsCode")
                .add("/highway/black/status/callback")
                .add("/highway/v2/black/status/callback")
                .add("/highway/v1/trucks/signStatus/callback")
                .add("/**/**/temporary/register")
                .add("/**/**/temporary/login")
                .add("/**/**/gzhInsurance/login")
                .add("/etc-order/v1/remote/api/getOrderByOrderNo")
                .add("/deposit/order/pay/app/notify")
                .add("/alipay/receive/payment/transfer")
                .add("/alipayNotify")
                .add("/mobile/order/refund/payment/pay")
                .add("/mobile/exchange/**")
                .add("/insurance/gzh/queryOrder")
                .add("/insurance/order/pay/wechat")
                .add("/exchange/ocpx/**")
                .add("/eapp/v1.0/withdraw/temp/qfu/check")
                .add("/mbr/order/notify/**")
                .add("/hk/order/notify/**")
                // .add("/insurance/order/**")
//                .add("/**")
                ;
        if(!"production".equals(profile)) {
//            patterns.add("/em/**/get-cmd-0015");
//            patterns.add("/em/**/get-cmd-0016");
//            patterns.add("/em/**/getCmd");
            patterns.add("/user/**/to-recover");
//            已激活OBU订单反复生成待结算接口
            patterns.add("/**/test/issue/mockWaitSettle");
        }
        registration.excludePathPatterns(patterns.toArray());
    }

    /**
     * 视博9901接口拦截器
     * @param registry
     */
    private void addCyxAPIInterceptor(InterceptorRegistry registry) {
        CheckSignInfoInterceptor interceptor = new CheckSignInfoInterceptor(apiSignConfig);
        InterceptorRegistration registration = registry.addInterceptor(interceptor);
        registration.addPathPatterns(PathPatterns.create()
                .add("/api/cyx/**")
                .toArray());
    }


    //    @GetMapping({"/", ""})
    @GetMapping("/")
    String index(Model model) {
        return "health.html";
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptor()).addPathPatterns(PathPatterns.create().add("/**").toArray());
        addTokenInterceptor(registry);
        addCyxAPIInterceptor(registry);
        registry.addInterceptor(repeatSubmitInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(exchangePhoneToolInterceptor()).addPathPatterns("/mobile/exchange/**").excludePathPatterns("/mobile/exchange/upload/device/files/info","/mobile/exchange/download/device/files/info","/mobile/exchange/review/status");
    }


}