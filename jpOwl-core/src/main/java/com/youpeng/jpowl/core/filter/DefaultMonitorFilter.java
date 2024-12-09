package com.youpeng.jpowl.core.filter;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.enums.MonitorEventType;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认监控过滤器
 * 提供基于事件类型和来源的过滤功能
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class DefaultMonitorFilter implements MonitorFilter {
    private final Set<MonitorEventType> enabledTypes;     // 启用的事件类型
    private final Set<String> excludedSources;            // 排除的事件来源
    private final ConcurrentHashMap<String, Long> rateLimits;  // 速率限制

    /**
     * 构造默认过滤器
     * 默认启用所有事件类型
     */
    public DefaultMonitorFilter() {
        this.enabledTypes = new HashSet<>();
        this.excludedSources = new HashSet<>();
        this.rateLimits = new ConcurrentHashMap<>();
        
        // 默认启用所有类型
        for (MonitorEventType type : MonitorEventType.values()) {
            enabledTypes.add(type);
        }
    }

    /**
     * 启用指定事件类型
     *
     * @param type 事件类型
     */
    public void enableType(MonitorEventType type) {
        enabledTypes.add(type);
    }

    /**
     * 禁用指定事件类型
     *
     * @param type 事件类型
     */
    public void disableType(MonitorEventType type) {
        enabledTypes.remove(type);
    }

    /**
     * 添加需要排除的来源
     *
     * @param source 来源标识
     */
    public void excludeSource(String source) {
        excludedSources.add(source);
    }

    /**
     * 设置来源的速率限制
     *
     * @param source 来源标识
     * @param ratePerSecond 每秒允许的事件数
     */
    public void setRateLimit(String source, long ratePerSecond) {
        rateLimits.put(source, ratePerSecond);
    }

    @Override
    public boolean accept(MonitorEvent event) {
        // 检查事件类型
        if (!enabledTypes.contains(event.getType())) {
            return false;
        }
        
        // 检查排除的来源
        if (excludedSources.contains(event.getSource())) {
            return false;
        }
        
        // 检查速率限制
        Long rateLimit = rateLimits.get(event.getSource());
        if (rateLimit != null) {
            long currentSecond = event.getTimestamp() / 1000;
            String key = event.getSource() + ":" + currentSecond;
            Long count = rateLimits.computeIfAbsent(key, k -> 0L);
            if (count >= rateLimit) {
                return false;
            }
            rateLimits.put(key, count + 1);
        }
        
        return true;
    }
} 