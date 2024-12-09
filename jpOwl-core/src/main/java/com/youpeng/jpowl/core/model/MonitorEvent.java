package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;
import java.time.Instant;

/**
 * 监控事件基类
 * 所有监控事件的基础类，定义了事件的基本属性
 * 
 * @author youpeng
 * @since 1.0.0
 */
public abstract class MonitorEvent {
    private final String id;              // 事件唯一标识
    private final MonitorEventType type;  // 事件类型
    private final long timestamp;         // 事件发生时间戳
    private final String source;          // 事件来源

    /**
     * 构造监控事件
     *
     * @param id 事件ID
     * @param type 事件类型
     * @param source 事件来源
     */
    protected MonitorEvent(String id, MonitorEventType type, String source) {
        this.id = id;
        this.type = type;
        this.timestamp = Instant.now().toEpochMilli();
        this.source = source;
    }

    // Getters
    public String getId() { return id; }
    public MonitorEventType getType() { return type; }
    public long getTimestamp() { return timestamp; }
    public String getSource() { return source; }
} 