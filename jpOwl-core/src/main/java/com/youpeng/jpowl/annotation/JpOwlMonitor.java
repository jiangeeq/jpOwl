package com.youpeng.jpowl.annotation;

import java.lang.annotation.*;

/**
 * 业务监控注解
 * 提供更丰富的监控配置选项
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Monitor
public @interface JpOwlMonitor {
    /**
     * 监控名称
     */
    String value() default "";
    
    /**
     * 业务标签
     */
    String[] tags() default {};
    
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
    
    /**
     * 业务阈值配置
     */
    BusinessThreshold[] businessThresholds() default {};
    
    /**
     * 追踪阈值配置
     */
    TraceThreshold[] traceThresholds() default {};
} 