package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;

/**
 * 指标监控事件
 * 用于记录系统和业务指标
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MetricEvent extends MonitorData {
    private final String metricName;
    private final double value;
    private final String unit;

    /**
     * 构造指标事件
     *
     * @param id 事件ID
     * @param source 事件来源
     * @param metricName 指标名称
     * @param value 指标值
     * @param unit 单位
     */
    public MetricEvent(String id, String source, String metricName, double value, String unit) {
        super(id, MonitorEventType.METRIC, source);
        this.metricName = metricName;
        this.value = value;
        this.unit = unit;
        
        // 添加指标相关数据
        addMetric(metricName, value);
        addTag("unit", unit);
    }

    public String getMetricName() { return metricName; }
    public double getValue() { return value; }
    public String getUnit() { return unit; }
} 