package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;
import com.youpeng.jpowl.core.util.CoreUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控事件构建器
 * 提供流式API创建各类监控事件
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MonitorEventBuilder {
    private String id;
    private String source;
    private Map<String, Object> healthData;
    
    private MonitorEventBuilder() {
        this.id = CoreUtils.generateId();
        this.source = CoreUtils.getHostName();
        this.healthData = new HashMap<>();
    }
    
    public static MonitorEventBuilder create() {
        return new MonitorEventBuilder();
    }
    
    public MonitorEventBuilder id(String id) {
        this.id = id;
        return this;
    }
    
    public MonitorEventBuilder source(String source) {
        this.source = source;
        return this;
    }
    
    /**
     * 构建事务事件
     */
    public TransactionEvent transaction(String name, long duration, boolean success) {
        return new TransactionEvent(id, source, name, duration, success);
    }
    
    /**
     * 构建指标事件
     */
    public MetricEvent metric(String name, double value, String unit) {
        return new MetricEvent(id, source, name, value, unit);
    }
    
    /**
     * 构建心跳事件
     */
    public HeartbeatEvent heartbeat(String serviceName, String status) {
        return new HeartbeatEvent(id, source, serviceName, status, healthData);
    }
    
    /**
     * 添加健康数据
     */
    public MonitorEventBuilder withHealthData(String key, Object value) {
        this.healthData.put(key, value);
        return this;
    }
} 