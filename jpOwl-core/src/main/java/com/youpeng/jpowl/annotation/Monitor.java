package com.youpeng.jpowl.annotation;

import java.lang.annotation.*;

/**
 * 基础监控注解
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
     * 是否启用
     */
    boolean enabled() default true;
    
    /**
     * 采样率(0-100)
     */
    int samplingRate() default 100;
}
