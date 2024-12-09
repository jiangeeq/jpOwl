package com.youpeng.jpowl.logging;

import com.youpeng.jpowl.logging.adapter.Log4j2Adapter;
import com.youpeng.jpowl.logging.adapter.LogbackAdapter;
import com.youpeng.jpowl.logging.adapter.LoggerAdapter;
import com.youpeng.jpowl.logging.exception.LogException;
import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志适配器工厂类
 * 负责创建和管理日志适配器实例
 */
public class LoggerFactory {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoggerFactory.class);
    private static final LoggerAdapter ADAPTER;
    private static final Map<String, LoggerAdapter> ADAPTERS = new ConcurrentHashMap<>();
    
    static {
        LoggerAdapter adapter = null;
        try {
            // 确保只初始化一个适配器
            if (isLogbackAvailable()) {
                adapter = new LogbackAdapter();
                logger.info("jpOwl使用Logback作为日志记录框架");
            } else if (isLog4j2Available()) {
                adapter = new Log4j2Adapter();
                logger.info("jpOwl使用Log4j2作为日志记录框架");
            }

            if (adapter == null || !adapter.isSupported()) {
                throw new LogException("没有找到支持的日志框架实现");
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError("初始化日志适配器失败: " + e.getMessage());
        }
        ADAPTER = adapter;
    }
    
    private static boolean isLogbackAvailable() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    private static boolean isLog4j2Available() {
        try {
            Class.forName("org.apache.logging.log4j.core.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    /**
     * 获取指定名称的日志适配器
     * @param name 日志器名称
     * @return 日志适配器实例
     */
    public static LoggerAdapter getLogger(String name) {
        return ADAPTERS.computeIfAbsent(name, k -> ADAPTER);
    }
    
    /**
     * 记录日志事件
     * @param event 日志事件
     */
    public static void log(LogEvent event) {
        getLogger(event.getLoggerName()).log(event);
    }
    
    /**
     * 设置日志级别
     * @param loggerName 日志器名称
     * @param level 目标级别
     */
    public static void setLevel(String loggerName, LogLevel level) {
        getLogger(loggerName).setLevel(loggerName, level);
    }
    
    /**
     * 获取当前日志级别
     * @param loggerName 日志器名称
     * @return 当前日志级别
     */
    public static LogLevel getLevel(String loggerName) {
        return getLogger(loggerName).getLevel(loggerName);
    }
    
    /**
     * 关闭所有日志适配器
     */
    public static void shutdown() {
        ADAPTERS.clear();
    }
} 