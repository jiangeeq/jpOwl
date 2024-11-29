package com.youpeng.jpowl.aggregator;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 聚合器和告警策略配置,秒级、分钟级监控及告警策略
 * 进行指标的收集和计算
 */
@Component
public class MetricAggregator {
    private static final ConcurrentMap<String, Long> failureCounts = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Long> executionCounts = new ConcurrentHashMap<>();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public MetricAggregator() {
        executorService.scheduleAtFixedRate(this::reportMetrics, 0, 1, TimeUnit.MINUTES);
    }

    static {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                    resetCounts();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    public static void incrementFailureCount(String key) {
        failureCounts.merge(key, 1L, Long::sum);
    }

    public static void incrementExecutionCount(String key) {
        executionCounts.merge(key, 1L, Long::sum);
    }

    public static long getFailureCount(String key) {
        return failureCounts.getOrDefault(key, 0L);
    }

    public static long getExecutionCount(String key) {
        return executionCounts.getOrDefault(key, 0L);
    }

    private static void resetCounts() {
        failureCounts.clear();
        executionCounts.clear();
    }

    private void reportMetrics() {
        // 报告指标的逻辑可以在超过阈值时触发警报
        failureCounts.forEach((key, count) -> {
            if (count > 10) {
                // Trigger alert
                System.out.println("Alert: High failure count for " + key);
            }
        });
        resetCounts();
    }
}