package com.youpeng.jpowl.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.youpeng.jpowl.annotation.MonitorPointLog;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 记录日志的输出次数: 你可以在日志的Appender中进行计数，或者在一个单独的计数器中记录日志的输出次数。
 * 动态调整日志级别: 基于计数器的值，动态调整日志的级别。
 */
public class CountingAppender extends AppenderBase<ILoggingEvent> {
    private static final int THRESHOLD_COUNT = 3; // Change as needed
    private final AtomicInteger count =  new AtomicInteger(0);

    @Override
    protected void append(ILoggingEvent eventObject) {
        // 获取日志内容
        String logMessage = eventObject.getFormattedMessage();

        // 获取调用方法名
        StackTraceElement[] callerData = eventObject.getCallerData();
        String methodName = callerData[0].getMethodName();
        String className = callerData[0].getClassName();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger targetLogger = context.getLogger(className);

        boolean hasPointMonitorLogAnnotation = getPointMonitorLog(className, methodName) !=null;
        if (hasPointMonitorLogAnnotation && count.incrementAndGet() >= THRESHOLD_COUNT) {
            synchronized (this) {
                if (count.get() >= THRESHOLD_COUNT) {
                    adjustLogLevel(logMessage,targetLogger);
                    count.set(0); // Reset count after changing the log level
                }
            }
        }else{
            int a = 1+1;
        }
    }

    private MonitorPointLog getPointMonitorLog(String className, String  methodName){

        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName);
            MonitorPointLog annotation = method.getAnnotation(MonitorPointLog.class);
            return annotation;
        } catch (ClassNotFoundException | NoSuchMethodException  e) {
            throw new RuntimeException(e);
        }
    }
    private void adjustLogLevel(String logMessage, Logger targetLogger ) {

        Level currentLevel = targetLogger.getEffectiveLevel();
        if (currentLevel.isGreaterOrEqual(Level.DEBUG)) {
            targetLogger.setLevel(Level.INFO);
            System.out.println("Log level changed to INFO "+logMessage);
        } else if (currentLevel.isGreaterOrEqual(Level.INFO)) {
            targetLogger.setLevel(Level.WARN);
            System.out.println("Log level changed to WARN");
        } else if (currentLevel.isGreaterOrEqual(Level.WARN)) {
            targetLogger.setLevel(Level.ERROR);
            System.out.println("Log level changed to ERROR");
        } else {
            targetLogger.setLevel(Level.DEBUG);
            System.out.println("Log level changed to DEBUG");
        }
    }
}

