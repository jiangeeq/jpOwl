package com.youpeng.jpowl.logging.filter;

import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;

/**
 * 日志级别过滤器
 */
public class LevelFilter implements LogFilter {
    private final LogLevel threshold;
    
    public LevelFilter(LogLevel threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public boolean accept(LogEvent event) {
        return event.getLevel().ordinal() >= threshold.ordinal();
    }
    
    @Override
    public LogEvent process(LogEvent event) {
        return event;
    }
} 