package com.youpeng.jpowl.logging.monitor;

import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 日志指标收集器
 */
public class LoggingMetrics {
    // 使用原子计数器确保线程安全
    private final Map<LogLevel, AtomicLong> levelCounters = new EnumMap<>(LogLevel.class);
    private final AtomicLong totalLogs = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong processingTime = new AtomicLong(0);
    private final AtomicLong droppedLogs = new AtomicLong(0);

    // 使用滑动窗口记录最近的日志率
    private final SlidingTimeWindowCounter logRateCounter;

    public LoggingMetrics() {
        // 初始化每个日志级别的计数器
        for (LogLevel level : LogLevel.values()) {
            levelCounters.put(level, new AtomicLong(0));
        }
        // 创建1分钟的滑动窗口计数器
        logRateCounter = new SlidingTimeWindowCounter(60_000);
    }

    /**
     * 记录日志事件
     */
    public void recordLogEvent(LogEvent event, long processingTimeNanos) {
        totalLogs.incrementAndGet();
        levelCounters.get(event.getLevel()).incrementAndGet();
        processingTime.addAndGet(processingTimeNanos);
        logRateCounter.increment();

        if (event.getLevel() == LogLevel.ERROR) {
            errorCount.incrementAndGet();
        }
    }

    /**
     * 记录被丢弃的日志
     */
    public void recordDroppedLog() {
        droppedLogs.incrementAndGet();
    }

    /**
     * 获取指标快照
     */
    public MetricsSnapshot getSnapshot() {
        ConcurrentHashMap<LogLevel, Long> computeLevelCounts = levelCounters.entrySet().stream()
                .collect(Collectors.toConcurrentMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get(),
                        Long::sum, // 合并值
                        ConcurrentHashMap::new
                ));

        return new MetricsSnapshot(
                totalLogs.get(),
                computeLevelCounts,
                errorCount.get(),
                processingTime.get(),
                droppedLogs.get(),
                logRateCounter.getRate()
        );
    }

    /**
     * 重置所有计数器
     */
    public void reset() {
        totalLogs.set(0);
        levelCounters.values().forEach(counter -> counter.set(0));
        errorCount.set(0);
        processingTime.set(0);
        droppedLogs.set(0);
        logRateCounter.reset();
    }
}