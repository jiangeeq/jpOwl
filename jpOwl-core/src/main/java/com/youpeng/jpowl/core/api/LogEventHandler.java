package com.youpeng.jpowl.core.api;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.exception.LogException;

/**
 * 日志事件处理器接口
 */
public interface LogEventHandler {
    /**
     * 处理日志事件
     */
    void handle(MonitorEvent event) throws LogException;
    
    /**
     * 获取处理器名称
     */
    String getName();
    
    /**
     * 是否支持该事件
     */
    boolean supports(MonitorEvent event);
} 