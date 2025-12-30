package com.anyi.sparrow.cyx.config;

import com.anyi.sparrow.cyx.filter.CyxApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class CyxApiFilterConfig {

    @Bean
    public FilterRegistrationBean cyxApiFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(cyxApiFilter());
        registrationBean.setName("cyxApiFilter");
        registrationBean.addUrlPatterns("/api/cyx/*");
        registrationBean.setOrder(0);

        return  registrationBean;
    }

    @Bean
    public Filter cyxApiFilter() {
        return new CyxApiFilter();
    }
}
