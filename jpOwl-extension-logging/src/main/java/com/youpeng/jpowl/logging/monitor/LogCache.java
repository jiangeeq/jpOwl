package com.youpeng.jpowl.logging.monitor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 日志缓存管理器
 * 提供日志事件的缓存和检索功能
 */
public class LogCache {
    // 缓存对象，用于存储日志事件
    private final Cache<String, LogEvent> cache;
    // 缓存的最大大小
    private final int maxSize;

    /**
     * 构造函数，初始化缓存
     *
     * @param maxSize 缓存的最大大小
     */
    public LogCache(int maxSize) {
        this.maxSize = maxSize;
        // 创建缓存，设置最大大小、过期时间和统计信息
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .recordStats()
            .build();
    }

    /**
     * 缓存日志事件
     *
     * @param event 要缓存的日志事件
     */
    public void cache(LogEvent event) {
        // 生成缓存键值
        String key = generateKey(event);
        // 将日志事件放入缓存
        cache.put(key, event);
    }

    /**
     * 根据条件查询日志
     *
     * @param condition 查询条件
     * @return 匹配条件的日志事件列表
     */
    public List<LogEvent> search(Predicate<LogEvent> condition) {
        // 从缓存中筛选出匹配条件的日志事件
        return cache.asMap().values().stream()
            .filter(condition)
            .collect(Collectors.toList());
    }

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    public CacheStats getStats() {
        // 返回缓存的统计信息
        return cache.stats();
    }

    /**
     * 生成缓存键值
     *
     * @param event 日志事件
     * @return 缓存键值
     */
    private String generateKey(LogEvent event) {
        // 根据日志事件的时间戳、记录器名称和级别生成唯一键值
        return String.format("%d-%s-%s",
            event.getTimestamp(),
            event.getLoggerName(),
            event.getLevel());
    }
}
