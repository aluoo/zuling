package com.anyi.common.aspect;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/17
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Pointcut("@annotation(com.anyi.common.aspect.WebLog)")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            // 开始打印请求日志
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // 获取 @WebLog 注解的描述信息
            String methodDescription = getAspectLogDescription(joinPoint);
            Object[] args = joinPoint.getArgs();
            String requestArgs = "";
            if (args != null) {
                for (Object arg : args) {
                    if (arg instanceof MultipartFile) {
                        requestArgs = "upload files";
                    }
                }
            }
            if (StringUtils.isEmpty(requestArgs)) {
                requestArgs = JSONUtil.toJsonStr(args);
            }

            // 打印请求相关参数
            log.info("========================================== Start ==========================================");
            // 打印请求 url
            log.info("URL            : {}", request.getRequestURL().toString());
            // 打印描述信息
            log.info("Description    : {}", methodDescription);
            // 打印 Http method
            log.info("HTTP Method    : {}", request.getMethod());
            // 打印调用 controller 的全路径以及执行方法
            log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            // 打印请求的 IP
            log.info("IP             : {}", request.getRemoteAddr());
            // 打印请求入参
            log.info("Request Args   : {}", requestArgs);
        } catch (Exception e) {
            String stackTrace = ExceptionUtils.getStackTrace(e);
            log.error("WebLogAspect.doBefore.error:{}", stackTrace);
        }
    }

    @After("webLog()")
    public void doAfter() {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        if (getPrintResponse(proceedingJoinPoint)) {
            // 打印出参
            log.info("Response Args  : {}", JSONUtil.toJsonStr(result, JSONConfig.create().setDateFormat(DatePattern.NORM_DATETIME_PATTERN)));
        }
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        log.info("=========================================== End ===========================================" + LINE_SEPARATOR );
        return result;
    }

    private String getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(WebLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }

    private boolean getPrintResponse(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        boolean printResponse = true;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    printResponse = method.getAnnotation(WebLog.class).printResponse();
                    break;
                }
            }
        }
        return printResponse;
    }
}