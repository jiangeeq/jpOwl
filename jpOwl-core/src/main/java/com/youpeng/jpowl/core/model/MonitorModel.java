package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.AlertLevel;
import java.time.Instant;

/**
 * 监控数据模型
 */
public class MonitorModel {
    private String id;
    private AlertLevel level;
    private String title;
    private String content;
    private long timestamp;
    private String source;
    private Object data;

    public MonitorModel(String id, AlertLevel level, String title, 
                       String content, String source, Object data) {
        this.id = id;
        this.level = level;
        this.title = title;
        this.content = content;
        this.timestamp = Instant.now().toEpochMilli();
        this.source = source;
        this.data = data;
    }

    // Getters
    public String getId() { return id; }
    public AlertLevel getLevel() { return level; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
    public String getSource() { return source; }
    public Object getData() { return data; }
} 