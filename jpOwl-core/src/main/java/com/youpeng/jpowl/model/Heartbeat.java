package com.youpeng.jpowl.model;

import java.util.HashMap;
import java.util.Map;

public class Heartbeat extends MonitorModel {
    private String name;
    private long timestamp;
    private Map<String, Object> metrics;

    public Heartbeat(String name) {
        super("","");
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.metrics = new HashMap<>();
    }

    public void addMetric(String key, Object value) {
        this.metrics.put(key, value);
    }

    @Override
    public String toString() {
        return String.format("Heartbeat[name=%s, timestamp=%d, metrics=%s]", name, timestamp, metrics);
    }

    @Override
    public String serialize() {
        return "Heartbeat{name='" + getMessage() + "', status='" + timestamp + "'}";
    }
}