package com.youpeng.jpowl.alert.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 监控数据模型
 * 用于封装需要告警的监控信息
 */
public class MonitorModel {
    /**
     * 告警级别
     */
    private AlertLevel level;
    
    /**
     * 告警标题
     */
    private String title;
    
    /**
     * 告警内容
     */
    private String content;
    
    /**
     * 告警时间
     */
    private LocalDateTime timestamp;
    
    /**
     * 告警标签
     */
    private Map<String, String> tags;
    
    /**
     * 告警源
     */
    private String source;


    public AlertLevel getLevel() {
        return level;
    }

    public void setLevel(AlertLevel level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

