package com.youpeng.jpowl.logging.process;

import com.youpeng.jpowl.logging.decorator.LogEventDecorator;
import com.youpeng.jpowl.logging.exception.LogHandlerException;
import com.youpeng.jpowl.logging.filter.CompositeFilter;
import com.youpeng.jpowl.logging.filter.LogFilter;
import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.handler.LogEventHandler;
import com.youpeng.jpowl.logging.monitor.LoggingMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * 日志处理链
 * 负责组织和执行日志的处理流程
 */
public class LogProcessingChain {
    private final CompositeFilter filter = new CompositeFilter();
    private final List<LogEventHandler> handlers = new ArrayList<>();
    private final List<LogEventDecorator> decorators = new ArrayList<>();
    private final LoggingMetrics metrics;

    public LoggingMetrics getMetrics() {
        return metrics;
    }

    public LogProcessingChain(LoggingMetrics metrics) {
        this.metrics = metrics;
    }

    public void addDecorator(LogEventDecorator decorator) {
        decorators.add(decorator);
    }
    /**
     * 添加过滤器
     */
    public void addFilter(LogFilter logFilter) {
        filter.addFilter(logFilter);
    }
    
    /**
     * 添加处理器
     */
    public void addHandler(LogEventHandler handler) {
        handlers.add(handler);
        // 根据优先级排序
        handlers.sort(Comparator.comparingInt(LogEventHandler::getOrder));
    }
    
    /**
     * 处理日志事件
     */
    public void process(LogEvent event) {
        // 记录处理开始时间
        long startTime = System.nanoTime();
        
        // 1. 先进行过滤
        if (!filter.accept(event)) {
            metrics.recordDroppedLog();
            return;
        }
        
        LogEvent processedEvent = filter.process(event);
        if (processedEvent == null) {
            metrics.recordDroppedLog();
            return;
        }
        // 2. 进行装饰
        LogEvent decoratedEvent = decorators.stream()
                .reduce(event,
                        (e, decorator) -> decorator.decorate(e),
                        (e1, e2) -> e1);
        // 3. 执行处理器
        for (LogEventHandler handler : handlers) {
            try {
                LogEvent handledEvent = handler.handle(processedEvent);
                if (handledEvent == null) {
                    metrics.recordDroppedLog();
                    return;
                }
                processedEvent = handledEvent;
            } catch (LogHandlerException e) {
                metrics.recordDroppedLog();
                // 处理异常
            }
        }
        
        // 记录处理完成时间并更新指标
        long processingTime = System.nanoTime() - startTime;
        metrics.recordLogEvent(processedEvent, processingTime);
    }
} 