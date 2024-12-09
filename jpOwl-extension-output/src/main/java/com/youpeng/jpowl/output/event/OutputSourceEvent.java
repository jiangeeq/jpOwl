package com.youpeng.jpowl.output.event;

import com.youpeng.jpowl.output.core.OutputSourceType;

import java.time.Instant;
import java.util.UUID;

/**
 * 输出源事件
 */
public class OutputSourceEvent {
    /**
     * 事件类型
     */
    private EventType type;
    
    /**
     * 输出源类型
     */
    private OutputSourceType sourceType;
    
    /**
     * 事件时间
     */
    private Instant timestamp;
    
    /**
     * 事件详情
     */
    private String details;
    
    /**
     * 相关异常
     */
    private Throwable throwable;



    public enum EventType {
        STARTED,
        STOPPED,
        WRITE_SUCCESS,
        WRITE_FAILED,
        ERROR,
        LOAD_STATUS,
        OVERLOADED
    }

    public static OutputSourceEvent create() {
        return new OutputSourceEvent();
    }
    public OutputSourceEvent type(EventType type) {
        this.type = type;
        return this;
    }

    public OutputSourceEvent sourceType(OutputSourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public OutputSourceEvent timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public OutputSourceEvent details(String details) {
        this.details = details;
        return this;
    }

    public OutputSourceEvent throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }



    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public OutputSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(OutputSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public OutputSourceEvent build() {
        return this;
    }
}