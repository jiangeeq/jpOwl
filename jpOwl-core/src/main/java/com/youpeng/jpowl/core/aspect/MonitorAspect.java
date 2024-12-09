package com.youpeng.jpowl.core.aspect;

import com.youpeng.jpowl.core.annotation.Monitor;
import com.youpeng.jpowl.core.context.MonitorContext;
import com.youpeng.jpowl.core.model.MonitorData;
import com.youpeng.jpowl.core.monitor.MonitorManager;
import com.youpeng.jpowl.core.util.CoreUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监控切面
 * 处理带有@Monitor注解的方法调用
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Aspect
public class MonitorAspect {
    private static final Logger logger = LoggerFactory.getLogger(MonitorAspect.class);
    
    private final MonitorManager monitorManager;

    public MonitorAspect(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    @Around("@annotation(monitor)")
    public Object around(ProceedingJoinPoint point, Monitor monitor) throws Throwable {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringType().getSimpleName();
        
        // 构建监控名称
        String monitorName = monitor.value().isEmpty() ? 
            className + "." + methodName : monitor.value();
            
        // 开始监控
        MonitorContext context = monitorManager.startMonitor(monitorName);
        if (context == null) {
            return point.proceed();
        }
        
        long startTime = System.currentTimeMillis();
        try {
            // 执行目标方法
            Object result = point.proceed();
            
            // 记录监控数据
            recordSuccess(context, monitor, startTime, point.getArgs(), result);
            
            return result;
        } catch (Throwable e) {
            // 记录异常信息
            recordError(context, monitor, startTime, point.getArgs(), e);
            throw e;
        } finally {
            monitorManager.endMonitor(context);
        }
    }
    
    private void recordSuccess(
            MonitorContext context, 
            Monitor monitor,
            long startTime,
            Object[] args,
            Object result) {
        try {
            MonitorData data = context.getMonitorData();
            if (data != null) {
                // 记录执行时间
                data.addMetric("duration", System.currentTimeMillis() - startTime);
                
                // 记录参数
                if (monitor.logParams() && args != null) {
                    for (int i = 0; i < args.length; i++) {
                        data.addTag("param" + i, String.valueOf(args[i]));
                    }
                }
                
                // 记录返回值
                if (monitor.logResult() && result != null) {
                    data.addTag("result", String.valueOf(result));
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to record monitor data", e);
        }
    }
    
    private void recordError(
            MonitorContext context,
            Monitor monitor,
            long startTime,
            Object[] args,
            Throwable error) {
        try {
            MonitorData data = context.getMonitorData();
            if (data != null) {
                // 记录执行时间
                data.addMetric("duration", System.currentTimeMillis() - startTime);
                
                // 记录异常信息
                if (monitor.logException()) {
                    data.addTag("error", error.getClass().getName());
                    data.addTag("errorMessage", error.getMessage());
                }
                
                // 记录参数
                if (monitor.logParams() && args != null) {
                    for (int i = 0; i < args.length; i++) {
                        data.addTag("param" + i, String.valueOf(args[i]));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to record monitor data", e);
        }
    }
} 