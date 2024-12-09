package com.youpeng.jpowl.annotation;

import java.lang.annotation.*;

/**
 * 追踪阈值注解
 * 用于配置方法执行的追踪阈值
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TraceThreshold {
    /**
     * 执行时间阈值(毫秒)
     */
    long duration() default 1000L;
    
    /**
     * 异常次数阈值
     */
    int errorCount() default 5;
    
    /**
     * 时间窗口(秒)
     */
    int timeWindow() default 60;
    
    /**
     * 触发时的日志级别
     */
    String logLevel() default "DEBUG";
    
    /**
     * 是否启用堆栈追踪
     */
    boolean enableStackTrace() default false;
} 