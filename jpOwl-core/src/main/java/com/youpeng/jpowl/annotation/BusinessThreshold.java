package com.youpeng.jpowl.annotation;

import java.lang.annotation.*;

/**
 * 业务阈值注解
 * 用于配置业务监控的阈值
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessThreshold {
    /**
     * 阈值名称
     */
    String name();
    
    /**
     * 阈值值
     */
    double value();
    
    /**
     * 比较操作符
     */
    String operator() default ">";
    
    /**
     * 告警级别
     */
    String alertLevel() default "WARNING";
    
    /**
     * 告警消息模板
     */
    String alertTemplate() default "";
} 