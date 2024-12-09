package com.youpeng.jpowl.annotation;

import java.lang.annotation.*;

/**
 * 监控点日志注解
 * 用于记录方法调用的详细日志
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorPointLog {
    /**
     * 日志点名称
     */
    String value() default "";
    
    /**
     * 日志级别
     */
    String level() default "INFO";
    
    /**
     * 是否记录入参
     */
    boolean logInput() default true;
    
    /**
     * 是否记录出参
     */
    boolean logOutput() default true;
    
    /**
     * 是否记录执行时间
     */
    boolean logDuration() default true;
}
