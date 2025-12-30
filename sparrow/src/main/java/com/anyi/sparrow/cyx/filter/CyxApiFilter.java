package com.anyi.sparrow.cyx.filter;

import com.anyi.sparrow.cyx.wrapper.CyxApiRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤器(用于二次读取请求体内容)
 *
 * @author shenbinhong
 * @date 2022年02月08日 17:06
 */
//@Component
//@WebFilter(filterName = "cyxApiFilter", urlPatterns = {"/api/cyx/*"})
@Slf4j
public class CyxApiFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            ServletRequest requestWrapper = null;
            if (servletRequest instanceof HttpServletRequest) {
                requestWrapper = new CyxApiRequestWrapper((HttpServletRequest) servletRequest);
            }
            if (requestWrapper == null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }

}
