package com.youpeng.jpowl.core.api;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.enums.LogLevel;

/**
 * 日志适配器接口
 */
public interface LogAdapter {
    /**
     * 是否支持该日志框架
     */
    boolean isSupported();
    
    /**
     * 记录日志
     */
    void log(MonitorEvent event);
    
    /**
     * 设置日志级别
     */
    void setLevel(String loggerName, LogLevel level);
    
    /**
     * 获取日志级别
     */
    LogLevel getLevel(String loggerName);
} 