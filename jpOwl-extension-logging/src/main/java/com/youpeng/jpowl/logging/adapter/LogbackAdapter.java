package com.youpeng.jpowl.logging.adapter;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import com.youpeng.jpowl.logging.exception.LogException;
import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Objects;

/**
 * Logback日志框架适配器实现
 */
public class LogbackAdapter implements LoggerAdapter {
    private final LoggerContext loggerContext;
    
    public LogbackAdapter() throws LogException {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        if (!(factory instanceof LoggerContext)) {
            String eMessage = String.format("LoggerFactory 不是一个 Logback 的 LoggerContext，但 Logback 已在类路径中。请移除 Logback 或竞争的实现（%s 从 %s 加载）", factory.getClass(), this.getLocation(factory));
            throw new LogException(eMessage);
        }
        this.loggerContext = (LoggerContext) factory;
    }

    /**
     * 获取日志工厂类的代码位置
     * 此方法尝试获取指定日志工厂类的保护域，并从中提取代码源的位置信息
     * 主要用于诊断和调试，以了解日志工厂的来源
     *
     * @param factory 日志工厂实例，用于生成位置信息
     * @return Object 返回代码源的位置信息，如果无法获取则返回"unknown location"
     */
    private Object getLocation(ILoggerFactory factory) {
        try {
            // 获取日志工厂类的保护域
            ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
            // 从保护域中获取代码源
            CodeSource codeSource = protectionDomain.getCodeSource();
            // 如果代码源存在，则返回其位置信息
            if (codeSource != null) {
                return codeSource.getLocation();
            }
        } catch (SecurityException var4) {
            // 捕获安全异常，不做任何处理，直接跳过
        }

        // 如果无法获取位置信息或发生异常，则返回"unknown location"
        return "unknown location";
    }

    private String getLoggerName(String name) {
        return Objects.nonNull(name) && !"ROOT".equals(name) ? name : "ROOT";
    }
    
    @Override
    public boolean isSupported() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    @Override
    public void log(LogEvent event) {
        Logger logger = loggerContext.getLogger(event.getLoggerName());
        Level level = convertLevel(event.getLevel());
        LoggingEvent loggingEvent = createLoggingEvent(event, logger, level);

        // 方式1：完整处理继承,效果和log.info类似
        if (logger.isAdditive()) {  // 假设 logger 添加了这个属性
            logger.callAppenders(loggingEvent);
            logger.iteratorForAppenders().forEachRemaining(appender -> appender.doAppend(loggingEvent));
        } else {
            // 方式2：只处理当前logger appender
            logger.callAppenders(loggingEvent);
        }
    }
    /**
     * 创建Logback的LoggingEvent对象
     */
    private LoggingEvent createLoggingEvent(LogEvent event, Logger logger, Level level) {
        LoggingEvent loggingEvent = new LoggingEvent();
        loggingEvent.setLoggerName(event.getLoggerName());
        loggingEvent.setLevel(level);
        loggingEvent.setMessage(event.getMessage());
        loggingEvent.setTimeStamp(event.getTimestamp());
        loggingEvent.setLoggerContextRemoteView(logger.getLoggerContext().getLoggerContextRemoteView());

        // 设置异常信息
        if (event.getThrowable() != null) {
            loggingEvent.setThrowableProxy(new ThrowableProxy(event.getThrowable()));
        }

        // 设置线程名称
        loggingEvent.setThreadName(Thread.currentThread().getName());

        return loggingEvent;
    }

    @Override
    public void setLevel(String loggerName, LogLevel level) {
       Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(convertLevel(level));
    }
    
    @Override
    public LogLevel getLevel(String loggerName) {
        Logger logger = loggerContext.getLogger(loggerName);
        return convertToLogLevel(logger.getLevel());
    }
    
    /**
     * 将LogLevel转换为Logback的Level
     */
    private Level convertLevel(LogLevel level) {
        switch (level) {
            case TRACE : return Level.TRACE;
            case DEBUG :return Level.DEBUG;
            case INFO : return Level.INFO;
            case WARN : return Level.WARN;
            case ERROR :return Level.ERROR;
            default : return Level.INFO;
        }
    }
    
    /**
     * 将Logback的Level转换为LogLevel
     */
    private LogLevel convertToLogLevel(Level level) {
        if (level == null) return LogLevel.INFO;
        switch (level.levelInt) {
            case Level.TRACE_INT : return  LogLevel.TRACE;
            case Level.DEBUG_INT : return  LogLevel.DEBUG;
            case Level.INFO_INT : return  LogLevel.INFO;
            case Level.WARN_INT : return  LogLevel.WARN;
            case Level.ERROR_INT : return  LogLevel.ERROR;
            default : return  LogLevel.INFO;
        }
    }
} 