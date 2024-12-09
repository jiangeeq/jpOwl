package com.youpeng.jpowl.output.model;

import java.time.Instant;
import java.util.Map;

/**
 * 监控数据模型
 */
public class MonitorData {
    /**
     * 时间戳
     */
    private Instant timestamp;
    
    /**
     * 指标名称
     */
    private String metricName;
    
    /**
     * 指标值
     */
    private double value;
    
    /**
     * 标签
     */
    private Map<String, String> tags;
    
    /**
     * 附加属性
     */
    private Map<String, Object> attributes;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setData(MonitorData data) {
        this.timestamp = data.getTimestamp();
        this.metricName = data.getMetricName();
        this.value = data.getValue();
        this.tags = data.getTags();
        this.attributes = data.getAttributes();
    }

}