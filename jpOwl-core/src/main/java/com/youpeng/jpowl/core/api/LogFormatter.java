package com.youpeng.jpowl.core.api;

import com.youpeng.jpowl.core.model.MonitorEvent;

/**
 * 日志格式化器接口
 */
public interface LogFormatter {
    /**
     * 格式化日志事件
     */
    String format(MonitorEvent event);
} 