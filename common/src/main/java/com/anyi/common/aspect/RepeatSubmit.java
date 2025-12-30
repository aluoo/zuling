package com.anyi.common.aspect;

import java.lang.annotation.*;

/**
 * @author WangWJ
 * @Description 自定义注解 - 防止重复提交
 * @Date 2024/3/21
 * @Copyright
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交，默认5000ms
     */
    int interval() default 5000;

    String errorMessage() default "请勿重复提交";
}