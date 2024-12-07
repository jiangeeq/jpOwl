package com.youpeng.jpowl.logging.formatter;

import com.youpeng.jpowl.logging.model.LogEvent;

/**
 * 日志格式化器接口
 */
public interface LogFormatter {
    /**
     * 格式化日志事件
     * @param event 日志事件
     * @return 格式化后的字符串
     */
    String format(LogEvent event);
} 