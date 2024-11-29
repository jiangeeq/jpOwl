package com.youpeng.jpowl.annotation;

import com.youpeng.jpowl.config.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {
    String value() default "";
    LogLevel logLevel() default LogLevel.INFO;
}
