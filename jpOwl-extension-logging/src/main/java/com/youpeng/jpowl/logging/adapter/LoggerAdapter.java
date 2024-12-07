package com.youpeng.jpowl.logging.adapter;

import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;

/**
 * 日志适配器接口
 * 定义统一的日志操作接口，用于适配不同的日志框架实现
 */
public interface LoggerAdapter {
    /**
     * 检查当前日志框架是否可用
     * @return true 如果当前日志框架可用
     */
    boolean isSupported();
    
    /**
     * 记录日志事件
     * @param event 日志事件对象
     */
    void log(LogEvent event);
    
    /**
     * 设置指定logger的日志级别
     * @param loggerName logger名称
     * @param level 目标日志级别
     */
    void setLevel(String loggerName, LogLevel level);
    
    /**
     * 获取指定logger的当前日志级别
     * @param loggerName logger名称
     * @return 当前日志级别
     */
    LogLevel getLevel(String loggerName);
    
    /**
     * 格式化日志消息
     * @param event 日志事件
     * @return 格式化后的消息
     */
    default String formatMessage(LogEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(event.getMessage());
        if (event.getThrowable() != null) {
            sb.append(" - ").append(event.getThrowable().getMessage());
        }
        return sb.toString();
    }
} 