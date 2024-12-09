package com.youpeng.jpowl.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    @Before("execution(* org.slf4j.Logger.debug(..)) || execution(* org.slf4j.Logger.info(..)) || execution(* org.slf4j.Logger.error(..))")
    public void logBefore() {
        // 在这里添加你的自定义逻辑
        System.out.println("Custom logic before logging");
    }
}
