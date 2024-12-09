package com.youpeng.jpowl.core.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 监控指标收集器
 * 提供计数器和度量值的收集功能
 * 
 * @author youpeng
 * @since 1.0.0
 */
public final class MetricsCollector {
    private static final Map<String, LongAdder> counters = new ConcurrentHashMap<>();
    private static final Map<String, LongAdder> gauges = new ConcurrentHashMap<>();
    private static final Map<String, LongAdder> histograms = new ConcurrentHashMap<>();
    
    private MetricsCollector() {}

    /**
     * 增加计数器值
     *
     * @param name 计数器名称
     */
    public static void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new LongAdder()).increment();
    }

    /**
     * 增加计数器值
     *
     * @param name 计数器名称
     * @param delta 增加值
     */
    public static void addCounter(String name, long delta) {
        counters.computeIfAbsent(name, k -> new LongAdder()).add(delta);
    }

    /**
     * 更新度量值
     *
     * @param name 度量名称
     * @param value 度量值
     */
    public static void updateGauge(String name, long value) {
        LongAdder gauge = gauges.computeIfAbsent(name, k -> new LongAdder());
        gauge.reset();
        gauge.add(value);
    }

    /**
     * 记录直方图数据
     *
     * @param name 直方图名称
     * @param value 数据值
     */
    public static void recordHistogram(String name, long value) {
        histograms.computeIfAbsent(name, k -> new LongAdder()).add(value);
    }

    /**
     * 获取计数器值
     *
     * @param name 计数器名称
     * @return 计数器当前值
     */
    public static long getCounter(String name) {
        return counters.getOrDefault(name, new LongAdder()).sum();
    }

    /**
     * 获取度量值
     *
     * @param name 度量名称
     * @return 度量当前值
     */
    public static long getGauge(String name) {
        return gauges.getOrDefault(name, new LongAdder()).sum();
    }

    /**
     * 获取直方图值
     *
     * @param name 直方图名称
     * @return 直方图累计值
     */
    public static long getHistogram(String name) {
        return histograms.getOrDefault(name, new LongAdder()).sum();
    }

    /**
     * 获取所有计数器
     *
     * @return 计数器名称到值的映射
     */
    public static Map<String, Long> getAllCounters() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        counters.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }

    /**
     * 获取所有度量值
     *
     * @return 度量名称到值的映射
     */
    public static Map<String, Long> getAllGauges() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        gauges.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }

    /**
     * 获取所有直方图值
     *
     * @return 直方图名称到值的映射
     */
    public static Map<String, Long> getAllHistograms() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        histograms.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }

    /**
     * 重置所有指标
     */
    public static void reset() {
        counters.clear();
        gauges.clear();
        histograms.clear();
    }
} 