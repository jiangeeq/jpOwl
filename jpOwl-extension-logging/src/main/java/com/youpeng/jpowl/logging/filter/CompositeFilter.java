package com.youpeng.jpowl.logging.filter;

import com.youpeng.jpowl.logging.filter.LogFilter;
import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合过滤器
 * 支持多个过滤器的组合使用
 */
public class CompositeFilter implements LogFilter {
    private final List<LogFilter> filters = new ArrayList<>();
    
    public void addFilter(LogFilter filter) {
        filters.add(filter);
    }
    
    @Override
    public boolean accept(LogEvent event) {
        for (LogFilter filter : filters) {
            if (!filter.accept(event)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public LogEvent process(LogEvent event) {
        LogEvent result = event;
        for (LogFilter filter : filters) {
            result = filter.process(result);
            if (result == null) {
                return null;
            }
        }
        return result;
    }
} 