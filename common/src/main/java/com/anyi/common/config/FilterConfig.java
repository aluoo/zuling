package com.anyi.common.config;

import com.anyi.common.filter.ReplaceStreamFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/21
 * @Copyright
 * @Version 1.0
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean replaceStreamFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(replaceStreamFilter());
        registrationBean.setName("streamFilter");
        registrationBean.addUrlPatterns("/*");

        return  registrationBean;
    }

    @Bean(name = "replaceStreamFilter")
    public Filter replaceStreamFilter() {
        return new ReplaceStreamFilter();
    }
}