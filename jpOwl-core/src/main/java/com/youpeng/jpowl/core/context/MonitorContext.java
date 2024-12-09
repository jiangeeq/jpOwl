package com.youpeng.jpowl.core.context;

import com.youpeng.jpowl.core.model.MonitorData;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控上下文
 * 用于在监控过程中传递数据
 */
public class MonitorContext {
    private final String traceId;
    private final long startTime;
    private final Map<String, Object> attributes;
    private MonitorData monitorData;
    private Throwable error;

    public MonitorContext(String traceId) {
        this.traceId = traceId;
        this.startTime = System.currentTimeMillis();
        this.attributes = new HashMap<>();
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setMonitorData(MonitorData data) {
        this.monitorData = data;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    // Getters
    public String getTraceId() { return traceId; }
    public long getStartTime() { return startTime; }
    public MonitorData getMonitorData() { return monitorData; }
    public Throwable getError() { return error; }
    public Map<String, Object> getAttributes() { return attributes; }
} 