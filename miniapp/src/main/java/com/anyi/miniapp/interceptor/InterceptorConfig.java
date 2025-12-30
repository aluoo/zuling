package com.anyi.miniapp.interceptor;

import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private EmployeeService employeeService;

    @Bean
    public RepeatSubmitInterceptor repeatSubmitInterceptor() {
        return new RepeatSubmitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor(redisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns(getIncludePathPatterns());


        registry.addInterceptor(new MobileInterceptor(redisTemplate, employeeService))
                .addPathPatterns("/mobile/**")
                .excludePathPatterns(getIncludePathPatterns());

        registry.addInterceptor(repeatSubmitInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(getIncludePathPatterns());
    }

    private ArrayList<String> getIncludePathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/pay/notify/**",
                "/webjars/**",
                "/**/swagger-resources/**",
                "/**/webjars/**",
                "/**/v2/**",
                "/**/swagger-ui.html",
                "/**/doc.html/**",
                "/error/**",
                "/health-check/**",
                "/wx/access/**",
                "/collect/**",
                // "/mobile/**",
                "/out/purchase/generate/link",
                "/mobile/customer/refund/payment/**",
                "/insurance/order/payment/**",
                "/company/staff/join",
        };
        Collections.addAll(list, urls);
        return list;
    }
}