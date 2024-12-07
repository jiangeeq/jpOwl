package com.youpeng.jpowl.logging.aggregator;

import com.google.common.util.concurrent.AtomicDouble;
import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 时间窗口日志聚合器
 * 在指定时间窗口内对日志进行聚合
 */
public class TimeWindowAggregator implements LogAggregator {
    // 时间窗口的大小
    private final Duration windowSize;
    // 日志类别统计
    private final Map<String, AtomicLong> categoryStats = new ConcurrentHashMap<>();
    // 日志级别统计
    private final Map<LogLevel, AtomicLong> levelStats = new ConcurrentHashMap<>();
    // 错误日志事件队列
    private final Queue<LogEvent> errorEvents = new ConcurrentLinkedQueue<>();
    // 总日志数
    private final AtomicLong totalCount = new AtomicLong();
    // 错误日志数
    private final AtomicLong errorCount = new AtomicLong();
    // 总响应时间
    private final AtomicDouble totalResponseTime = new AtomicDouble();
    // 当前时间窗口的开始时间，使用volatile保证线程间可见性
    private volatile long windowStart;

    /**
     * 构造函数
     *
     * @param windowSize 时间窗口的大小
     */
    public TimeWindowAggregator(Duration windowSize) {
        this.windowSize = windowSize;
        this.windowStart = System.currentTimeMillis();
    }

    /**
     * 添加日志事件
     *
     * @param event 日志事件
     */
    @Override
    public synchronized void add(LogEvent event) {
        // 检查时间窗口是否过期，如果过期则重置
        if (isWindowExpired()) {
            reset();
        }

        // 增加总日志数
        totalCount.incrementAndGet();
        // 更新类别统计
        categoryStats.computeIfAbsent(event.getLoggerName(), k -> new AtomicLong())
                    .incrementAndGet();
        // 更新级别统计
        levelStats.computeIfAbsent(event.getLevel(), k -> new AtomicLong())
                 .incrementAndGet();

        // 如果是错误日志，增加错误计数并保存错误事件
        if (event.getLevel() == LogLevel.ERROR) {
            errorCount.incrementAndGet();
            errorEvents.offer(event);
        }

        // 更新总响应时间
        Optional.ofNullable(event.getMdc().get("responseTime"))
               .map(Double::parseDouble)
               .ifPresent(totalResponseTime::addAndGet);
    }

    /**
     * 获取聚合结果
     *
     * @return 聚合结果
     */
    @Override
    public AggregationResult getResult() {
        // 检查时间窗口是否过期，如果过期则重置
        if (isWindowExpired()) {
            reset();
        }

        // 构建并返回聚合结果
        return AggregationResult.create()
            .count(totalCount.get())
            .errorCount(errorCount.get())
            .avgResponseTime(totalCount.get() > 0 ?
                totalResponseTime.get() / totalCount.get() : 0)
            .categoryStats(new HashMap<>(categoryStats.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()))))
            .levelStats(new HashMap<>(levelStats.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()))))
            .topErrors(errorEvents.stream()
                .map(LogEvent::getMessage)
                .limit(10)
                .collect(Collectors.toList()))
            .build();
    }

    /**
     * 重置聚合器状态
     */
    @Override
    public synchronized void reset() {
        // 重置时间窗口开始时间
        windowStart = System.currentTimeMillis();
        // 重置计数器和统计信息
        totalCount.set(0);
        errorCount.set(0);
        totalResponseTime.set(0);
        categoryStats.clear();
        levelStats.clear();
        errorEvents.clear();
    }

    /**
     * 检查时间窗口是否过期
     *
     * @return 如果时间窗口过期返回true，否则返回false
     */
    private boolean isWindowExpired() {
        return System.currentTimeMillis() - windowStart > windowSize.toMillis();
    }
}

