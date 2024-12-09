package com.youpeng.jpowl.spring.aop;

import com.youpeng.jpowl.annotation.JpOwlMonitor;
import com.youpeng.jpowl.context.MonitorContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class JpOwlMonitorAspect {

    @Around("@annotation(com.youpeng.jpowl.annotation.JpOwlMonitor)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        JpOwlMonitor monitor = signature.getMethod().getAnnotation(JpOwlMonitor.class);
        
        // 创建监控上下文
        MonitorContext context = createContext(monitor, point);
        
        try {
            // 前置处理
            beforeProceed(context);
            
            // 执行目标方法
            Object result = point.proceed();
            
            // 后置处理
            afterProceed(context, result);
            
            return result;
        } catch (Throwable e) {
            // 异常处理
            handleException(context, e);
            throw e;
        } finally {
            // 清理上下文
            cleanupContext(context);
        }
    }
    
    private MonitorContext createContext(JpOwlMonitor monitor, ProceedingJoinPoint point) {
        MonitorContext context = new MonitorContext();
        
        // 处理跟踪阈值
        if (monitor.traceThreshold().spanCount() > 0) {
            context.setTraceThreshold(
                TraceThresholdConfig.builder()
                    .spanCount(monitor.traceThreshold().spanCount())
                    .escalateLevel(monitor.traceThreshold().escalateLevel())
                    .build()
            );
        }
        
        // 处理业务阈值
        if (!monitor.businessThreshold().metric().isEmpty()) {
            context.setBusinessThreshold(
                BusinessThresholdConfig.builder()
                    .metric(monitor.businessThreshold().metric())
                    .threshold(monitor.businessThreshold().threshold())
                    .escalateLevel(monitor.businessThreshold().escalateLevel())
                    .build()
            );
        }
        
        return context;
    }
} 