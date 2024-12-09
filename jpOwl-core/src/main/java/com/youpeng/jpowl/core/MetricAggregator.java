package com.youpeng.jpowl.core;

import com.youpeng.jpowl.model.Metric;
import java.util.concurrent.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 指标聚合器
 */
public class MetricAggregator {
    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private final Map<String, Double> gauges = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MetricAggregator() {
        // 每分钟执行一次聚合
        scheduler.scheduleAtFixedRate(this::aggregate, 1, 1, TimeUnit.MINUTES);
    }

    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong()).incrementAndGet();
    }

    public void recordGauge(String name, double value) {
        gauges.put(name, value);
    }

    private void aggregate() {
        // 执行聚合计算
        counters.forEach((name, value) -> {
            // 处理计数器
            System.out.println(name + ": " + value.get());
            value.set(0); // 重置计数器
        });

        gauges.forEach((name, value) -> {
            // 处理度量值
            System.out.println(name + ": " + value);
        });
    }

    public void shutdown() {
        scheduler.shutdown();
    }
} 