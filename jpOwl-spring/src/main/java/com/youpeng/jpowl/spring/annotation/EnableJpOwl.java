package com.youpeng.jpowl.spring.annotation;

import com.youpeng.jpowl.spring.config.MonitorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用JpOwl监控的注解
 * 用于在Spring应用中启用JpOwl监控功能
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MonitorConfiguration.class)
public @interface EnableJpOwl {
    /**
     * 是否启用监控
     */
    boolean enabled() default true;
    
    /**
     * 扫描的包路径
     */
    String[] basePackages() default {};
    
    /**
     * 是否启用异步处理
     */
    boolean async() default true;
    
    /**
     * 是否启用指标收集
     */
    boolean metrics() default true;
    
    /**
     * 是否启用告警功能
     */
    boolean alert() default true;
} 