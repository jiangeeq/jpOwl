package com.youpeng.jpowl.core.aggregator;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.model.MonitorData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 监控事件聚合器
 */
public class MonitorAggregator {
    private final Map<String, LongAdder> eventCounts = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> eventDurations = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> errorCounts = new ConcurrentHashMap<>();
    
    public void record(MonitorEvent event) {
        String eventType = event.getType().name();
        eventCounts.computeIfAbsent(eventType, k -> new LongAdder()).increment();
        
        if (event instanceof MonitorData) {
            MonitorData data = (MonitorData) event;
            Object duration = data.getMetrics().get("duration");
            if (duration instanceof Number) {
                eventDurations.computeIfAbsent(eventType, k -> new LongAdder())
                    .add(((Number) duration).longValue());
            }
            
            if (data.getMetrics().containsKey("error")) {
                errorCounts.computeIfAbsent(eventType, k -> new LongAdder()).increment();
            }
        }
    }
    
    public Map<String, Long> getEventCounts() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        eventCounts.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }
    
    public Map<String, Long> getEventDurations() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        eventDurations.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }
    
    public Map<String, Long> getErrorCounts() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        errorCounts.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }
} 