package com.youpeng.jpowl.spring.annotation;

import java.lang.annotation.*;

/**
 * 方法监控注解
 * 用于标记需要监控执行时间和异常的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Monitor {
    /**
     * 监控描述信息
     */
    String description() default "";
    
    /**
     * 是否记录方法参数
     */
    boolean logParams() default false;
    
    /**
     * 是否记录返回值
     */
    boolean logResult() default false;
} 