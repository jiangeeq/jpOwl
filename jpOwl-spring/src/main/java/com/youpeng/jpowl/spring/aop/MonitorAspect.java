package com.youpeng.jpowl.spring.aop;

import com.youpeng.jpowl.core.JpOwlCore;
import com.youpeng.jpowl.spring.annotation.Monitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 监控切面，用于拦截带有 @Monitor 注解的方法
 * 实现方法执行时间统计和异常监控
 */
@Aspect
public class MonitorAspect {
    private static final Logger logger = LoggerFactory.getLogger(MonitorAspect.class);
    
    private final JpOwlCore jpOwlCore;
    
    public MonitorAspect(JpOwlCore jpOwlCore) {
        this.jpOwlCore = jpOwlCore;
    }
    
    /**
     * 环绕通知，处理带有 @Monitor 注解的方法
     * 
     * @param point 切点
     * @param monitor 监控注解
     * @return 方法执行结果
     * @throws Throwable 执行异常
     */
    @Around("@annotation(monitor)")
    public Object around(ProceedingJoinPoint point, Monitor monitor) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = buildMethodName(signature);
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = point.proceed();
            recordSuccess(methodName, startTime);
            return result;
        } catch (Throwable e) {
            recordError(methodName, e);
            throw e;
        }
    }
    
    /**
     * 构建方法名称，格式: 类名.方法名
     */
    private String buildMethodName(MethodSignature signature) {
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        return className + "." + methodName;
    }
    
    /**
     * 记录方法执行成功的信息
     */
    private void recordSuccess(String methodName, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        jpOwlCore.logTransaction(methodName, duration);
        if (logger.isDebugEnabled()) {
            logger.debug("Method [{}] execution completed in {} ms", methodName, duration);
        }
    }
    
    /**
     * 记录方法执行异常的信息
     */
    private void recordError(String methodName, Throwable e) {
        String errorMessage = StringUtils.hasText(e.getMessage()) ? e.getMessage() : e.getClass().getName();
        jpOwlCore.logEvent("error", String.format("Method [%s] failed: %s", methodName, errorMessage));
        logger.error("Method [{}] execution failed", methodName, e);
    }
} 