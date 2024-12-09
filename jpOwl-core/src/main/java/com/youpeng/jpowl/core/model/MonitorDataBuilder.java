package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控数据构建器
 */
public class MonitorDataBuilder {
    private String id;
    private MonitorEventType type;
    private String source;
    private final Map<String, Object> metrics = new HashMap<>();
    private final Map<String, String> tags = new HashMap<>();

    public static MonitorDataBuilder create() {
        return new MonitorDataBuilder();
    }

    public MonitorDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MonitorDataBuilder type(MonitorEventType type) {
        this.type = type;
        return this;
    }

    public MonitorDataBuilder source(String source) {
        this.source = source;
        return this;
    }

    public MonitorDataBuilder metric(String key, Object value) {
        metrics.put(key, value);
        return this;
    }

    public MonitorDataBuilder tag(String key, String value) {
        tags.put(key, value);
        return this;
    }

    public MonitorData build() {
        if (id == null || type == null || source == null) {
            throw new IllegalStateException("id, type and source are required");
        }
        
        MonitorData data = new MonitorData(id, type, source);
        metrics.forEach(data::addMetric);
        tags.forEach(data::addTag);
        return data;
    }
} 