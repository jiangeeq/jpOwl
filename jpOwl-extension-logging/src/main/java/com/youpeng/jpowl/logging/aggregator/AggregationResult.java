package com.youpeng.jpowl.logging.aggregator;

import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;

import java.util.*;

/**
 * 聚合结果
 */
/**
 * AggregationResult类用于封装聚合结果数据
 * 它提供了关于处理操作的统计信息，包括成功操作的数量、错误数量、平均响应时间，
 * 以及按类别和日志级别分类的统计信息此外，它还记录了最常见的错误
 */
public class AggregationResult {
    // 成功操作的计数
    private long count;

    // 发生错误的计数
    private long errorCount;

    // 平均响应时间
    private double avgResponseTime;

    // 按类别分类的统计信息
    private Map<String, Long> categoryStats = new HashMap<>();

    // 按日志级别分类的统计信息
    private Map<LogLevel, Long> levelStats = new HashMap<>();

    // 最常见的错误列表
    private List<String> topErrors = new ArrayList<>();


    // 私有构造函数
    private AggregationResult() {}

    public AggregationResult(long count, long errorCount, double avgResponseTime, Map<String, Long> stringLongMap, Map<LogLevel, Long> logLevelLongMap, List<String> strings) {
        this.count = count;
        this.errorCount = errorCount;
        this.avgResponseTime = avgResponseTime;
        this.categoryStats = stringLongMap;
        this.levelStats = logLevelLongMap;
        this.topErrors = strings;
    }

    // 静态工厂方法
    public static AggregationResult create() {
        return new AggregationResult();
    }

    // 链式 Builder 方法
    public AggregationResult count(long count) {
        this.count = count;
        return this;
    }

    public AggregationResult errorCount(long errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public AggregationResult avgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
        return this;
    }

    public AggregationResult categoryStats(Map<String, Long> categoryStats) {
        this.categoryStats = new HashMap<>(categoryStats);  // 创建副本以确保不可变性
        return this;
    }

    public AggregationResult addCategoryStat(String category, Long count) {
        this.categoryStats.put(category, count);
        return this;
    }

    public AggregationResult levelStats(Map<LogLevel, Long> levelStats) {
        this.levelStats = new HashMap<>(levelStats);  // 创建副本以确保不可变性
        return this;
    }

    public AggregationResult addLevelStat(LogLevel level, Long count) {
        this.levelStats.put(level, count);
        return this;
    }

    public AggregationResult topErrors(List<String> topErrors) {
        this.topErrors = new ArrayList<>(topErrors);  // 创建副本以确保不可变性
        return this;
    }

    public AggregationResult addTopError(String error) {
        this.topErrors.add(error);
        return this;
    }

    // 构建方法
    public AggregationResult build() {
        return new AggregationResult(
            count,
            errorCount,
            avgResponseTime,
            Collections.unmodifiableMap(categoryStats),
            Collections.unmodifiableMap(levelStats),
            Collections.unmodifiableList(topErrors)
        );
    }
} 