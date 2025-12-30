package com.anyi.common.advice;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.anyi.common.domain.param.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.profiles.active}")
    private String profile;

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<?> handException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        logger.error("request:{} error:[{}],{}", request.getRequestURI(), ExceptionUtil.getMessage(e), ExceptionUtil.stacktraceToString(e));
        logger.debug("remoteAddr={}", request.getRemoteAddr());
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.debug("客户端ip={}", ip);
        if (e instanceof BaseException) {
            BaseException exception = (BaseException) e;
            response.setStatus(200);
            if (exception.getCode() == 575) {//弹窗处理
                if (!"production".equals(profile)) {
                    return Response.failed(exception.getCode(), exception.getMessage(), exception.getExtMessage(), exception.getPosMsg());
                }
                return Response.failed(exception.getCode(), exception.getMessage(), null, exception.getPosMsg());
            } else {
                if (!"production".equals(profile)) {
                    return Response.failed(exception.getCode(), exception.getMessage(), exception.getExtMessage());
                }
                return Response.failed(exception.getCode(), exception.getMessage());
            }
        } else if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof BaseException) {
            //req类中代码验证抛出的异常，会被spring包装两层
            BaseException exception = (BaseException) e.getCause().getCause();
            response.setStatus(200);
            if (!"production".equals(profile)) {
                return Response.failed(exception.getCode(), exception.getMessage(), exception.getExtMessage());
            }
            return Response.failed(exception.getCode(), exception.getMessage());
        } else if (e instanceof MethodArgumentNotValidException || e instanceof MethodArgumentTypeMismatchException
                || e instanceof ConstraintViolationException) {
            response.setStatus(200);
            if (!"production".equals(profile)) {
                if (e instanceof MethodArgumentNotValidException) {
                    MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) e;
                    BindingResult result = argumentNotValidException.getBindingResult();
                    if (result.hasErrors()) {
                        String errorMsg = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
                        return Response.failed(SystemError.INVALID_PARAMETER, errorMsg);
                    }
                }
                return Response.failed(SystemError.INVALID_PARAMETER, e.toString());
            }
            return Response.failed(SystemError.INVALID_PARAMETER, SystemError.INVALID_PARAMETER.getMessage());
        } else if (e instanceof NoHandlerFoundException) {
            response.setStatus(200);
            return Response.failed(SystemError.PAGE_NOT_FOUND, SystemError.PAGE_NOT_FOUND.getMessage());
        } else {
            response.setStatus(200);
            if (!"production".equals(profile)) {
                return Response.failed(SystemError.SYSTEM_INTERNAL_ERROR, SystemError.SYSTEM_INTERNAL_ERROR.getMessage());
            }
            return Response.failed(SystemError.SYSTEM_INTERNAL_ERROR, SystemError.SYSTEM_INTERNAL_ERROR.getMessage());
        }
    }
}