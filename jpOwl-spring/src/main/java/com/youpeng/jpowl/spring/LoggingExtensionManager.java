package com.youpeng.jpowl.spring;

import com.youpeng.jpowl.logging.aggregator.AggregationResult;
import com.youpeng.jpowl.logging.aggregator.LogAggregator;
import com.youpeng.jpowl.logging.decorator.LogEventDecorator;
import com.youpeng.jpowl.logging.formatter.LogFormatter;
import com.youpeng.jpowl.logging.handler.LogEventHandler;
import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.monitor.LogCache;
import com.youpeng.jpowl.logging.process.LogProcessingChain;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 日志扩展点管理器
 * 管理和协调各种日志扩展功能
 */
public class LoggingExtensionManager {
    private final List<LogEventDecorator> decorators = new ArrayList<>();
    private final List<LogFormatter> formatters = new ArrayList<>();
    private final List<LogAggregator> aggregators = new ArrayList<>();
    private final List<LogEventHandler> handlers = new ArrayList<>();

    private final LogCache cache;
    private final LogProcessingChain processingChain;
    
    public LoggingExtensionManager(int cacheSize) {
        this.cache = new LogCache(cacheSize);
        this.processingChain = new LogProcessingChain();
    }
    
    /**
     * 处理日志事件
     */
    public void processEvent(LogEvent event) {
        // 1. 应用装饰器
        LogEvent decoratedEvent = event;
        for (LogEventDecorator decorator : decorators) {
            decoratedEvent = decorator.decorate(decoratedEvent);
        }
        
        // 2. 更新聚合器
        for (LogAggregator aggregator : aggregators) {
            aggregator.add(decoratedEvent);
        }
        
        // 3. 缓存事件
        cache.cache(decoratedEvent);

    }
    
    // 扩展点注册方法
    public void addDecorator(LogEventDecorator decorator) {
        decorators.add(decorator);
    }
    
    public void addFormatter(LogFormatter formatter) {
        formatters.add(formatter);
    }
    
    public void addAggregator(LogAggregator aggregator) {
        aggregators.add(aggregator);
    }
    
     public void addHandler(LogEventHandler handler) {
            handlers.add(handler);
     }
    /**
     * 获取所有可用的处理器
     */
    public Collection<LogEventHandler> getAvailableHandlers() {
        return Collections.unmodifiableCollection(handlers);
    }

    /**
     * 获取所有可用的装饰器
     */
    public Collection<LogEventDecorator> getAvailableDecorators() {
        return decorators.stream().filter(LogEventDecorator::isEnabled).collect(Collectors.toList());
    }

    /**
     * 根据名称获取处理器
     */
    public Optional<LogEventHandler> getHandler(String name) {
        return handlers.stream().filter(handler -> handler.getName().equals(name)).findFirst();
    }
    // 查询和统计方法
    public List<LogEvent> searchEvents(Predicate<LogEvent> condition) {
        return cache.search(condition);
    }
    
    public Map<LogAggregator, AggregationResult> getAggregationResults() {
        return aggregators.stream()
            .collect(Collectors.toMap(
                aggregator -> aggregator,
                LogAggregator::getResult
            ));
    }
} 