package com.youpeng.jpowl.spring.annotation;

import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

/**
 * 监控切点注解
 * 用于定义Spring AOP的监控切点
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorPointcut {
    /**
     * 切点表达式
     */
    @AliasFor("value")
    String expression() default "";
    
    /**
     * 切点表达式别名
     */
    @AliasFor("expression")
    String value() default "";
    
    /**
     * 切点优先级
     */
    int order() default 0;
    
    /**
     * 是否启用
     */
    boolean enabled() default true;
} 