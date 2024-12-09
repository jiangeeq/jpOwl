package com.youpeng.jpowl.context;

import com.youpeng.jpowl.model.MonitorModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;

@Data
public class MonitorContext {
    private final List<MonitorModel> models = new CopyOnWriteArrayList<>();
    private final long startTime;
    private String currentLogLevel;
    private TraceThresholdConfig traceThreshold;
    private BusinessThresholdConfig businessThreshold;
    private ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<>();

    public MonitorContext() {
        this.startTime = System.currentTimeMillis();
    }

    public void addModel(MonitorModel model) {
        models.add(model);
    }

    public List<MonitorModel> getModels() {
        return new ArrayList<>(models);
    }

    public long getDuration() {
        return System.currentTimeMillis() - startTime;
    }

    public void escalateLogLevel(String newLevel) {
        // 实现日志级别调整逻辑
    }

    public void checkThresholds() {
        checkTraceThreshold();
        checkBusinessThreshold();
    }

    private void checkTraceThreshold() {
        if (traceThreshold != null && getCurrentSpanCount() > traceThreshold.getSpanCount()) {
            escalateLogLevel(traceThreshold.getEscalateLevel());
        }
    }

    private void checkBusinessThreshold() {
        if (businessThreshold != null) {
            Object metricValue = attributes.get(businessThreshold.getMetric());
            if (metricValue != null && exceedsThreshold(metricValue, businessThreshold.getThreshold())) {
                escalateLogLevel(businessThreshold.getEscalateLevel());
            }
        }
    }
} 