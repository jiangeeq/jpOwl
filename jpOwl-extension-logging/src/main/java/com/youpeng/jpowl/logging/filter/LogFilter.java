package com.youpeng.jpowl.logging.filter;

import com.youpeng.jpowl.logging.model.LogEvent;

/**
 * 日志过滤器接口
 * 用于过滤和处理日志事件
 */
public interface LogFilter {
    /**
     * 判断是否接受该日志事件
     * @param event 日志事件
     * @return true表示接受该事件,false表示拒绝
     */
    boolean accept(LogEvent event);
    
    /**
     * 处理日志事件
     * @param event 日志事件
     * @return 处理后的事件,返回null表示丢弃该事件
     */
    LogEvent process(LogEvent event);
} 