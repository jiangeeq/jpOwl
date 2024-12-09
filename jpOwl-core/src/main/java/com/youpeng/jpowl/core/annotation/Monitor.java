package com.youpeng.jpowl.core.annotation;

import com.youpeng.jpowl.core.enums.LogLevel;
import java.lang.annotation.*;

/**
 * 监控注解
 * 用于标记需要监控的方法或类
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Monitor {
    /**
     * 监控名称
     */
    String value() default "";
    
    /**
     * 日志级别
     */
    LogLevel level() default LogLevel.INFO;
    
    /**
     * 采样率(0-100)
     */
    int samplingRate() default 100;
    
    /**
     * 是否启用告警
     */
    boolean enableAlert() default false;
    
    /**
     * 告警阈值(毫秒)
     */
    long threshold() default 1000L;
    
    /**
     * 是否记录参数
     */
    boolean logParams() default false;
    
    /**
     * 是否记录返回值
     */
    boolean logResult() default false;
    
    /**
     * 是否记录异常
     */
    boolean logException() default true;
} 