package com.youpeng.jpowl.aspect;

import com.youpeng.jpowl.annotation.Monitor;
import com.youpeng.jpowl.core.LogManager;
import com.youpeng.jpowl.core.MonitorManager;
import com.youpeng.jpowl.model.Event;
import com.youpeng.jpowl.model.MonitorModel;
import com.youpeng.jpowl.model.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MonitorAspect {
    @Autowired
    private MonitorManager monitorManager;
    private LogManager  logManager = new LogManager();

    @Around("@annotation(monitor)")
    public Object around(ProceedingJoinPoint joinPoint, Monitor monitor) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            String message =   joinPoint.getSignature() + ", Execution time: " + (endTime - startTime) + " ms";
            MonitorModel model = new Transaction(message);
            monitorManager.log(model, monitor.logLevel());
            return result;
        } catch (Throwable throwable) {
            String message = "Method: " + joinPoint.getSignature() + ", Exception: " + throwable.getMessage();
            MonitorModel model = new Event("", message, "");
            monitorManager.log(model, monitor.logLevel());
            throw throwable;
        }
    }
}
