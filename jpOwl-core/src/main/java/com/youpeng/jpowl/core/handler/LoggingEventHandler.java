package com.youpeng.jpowl.core.handler;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.model.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志处理器
 * 将监控事件记录到日志
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class LoggingEventHandler implements MonitorEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingEventHandler.class);
    
    @Override
    public void handle(MonitorEvent event) {
        if (event instanceof MonitorData) {
            MonitorData data = (MonitorData) event;
            logger.info("Monitor event - Type: {}, Source: {}, Metrics: {}, Tags: {}",
                data.getType(), data.getSource(), data.getMetrics(), data.getTags());
        }
    }
    
    @Override
    public String getName() {
        return "LoggingEventHandler";
    }
    
    @Override
    public boolean supports(MonitorEvent event) {
        return true;  // 支持所有事件
    }
    
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;  // 最高优先级
    }
} 