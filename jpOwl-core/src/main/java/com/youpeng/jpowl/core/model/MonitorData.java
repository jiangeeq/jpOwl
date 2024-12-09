package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控数据类
 * 继承自MonitorEvent，用于存储具体的监控指标和标签
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MonitorData extends MonitorEvent {
    private final Map<String, Object> metrics;  // 监控指标
    private final Map<String, String> tags;     // 标签信息

    /**
     * 构造监控数据
     *
     * @param id 数据ID
     * @param type 事件类型
     * @param source 数据来源
     */
    public MonitorData(String id, MonitorEventType type, String source) {
        super(id, type, source);
        this.metrics = new HashMap<>();
        this.tags = new HashMap<>();
    }

    /**
     * 添加监控指标
     *
     * @param key 指标名称
     * @param value 指标值
     * @throws IllegalArgumentException 如果key或value为null
     */
    public void addMetric(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Metric key and value cannot be null");
        }
        metrics.put(key, value);
    }

    /**
     * 添加标签
     *
     * @param key 标签名
     * @param value 标签值
     * @throws IllegalArgumentException 如果key或value为null
     */
    public void addTag(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Tag key and value cannot be null");
        }
        tags.put(key, value);
    }

    /**
     * 获取所有监控指标
     * 返回指标的副本以保护内部状态
     *
     * @return 监控指标映射表的副本
     */
    public Map<String, Object> getMetrics() {
        return new HashMap<>(metrics);
    }

    /**
     * 获取所有标签
     * 返回标签的副本以保护内部状态
     *
     * @return 标签映射表的副本
     */
    public Map<String, String> getTags() {
        return new HashMap<>(tags);
    }
} 