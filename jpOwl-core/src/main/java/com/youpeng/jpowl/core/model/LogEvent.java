package com.youpeng.jpowl.core.model;

import java.time.Instant;
import java.util.Map;

/**
 * 核心日志事件模型
 */
public class LogEvent {
    private String loggerName;
    private LogLevel level;
    private String message;
    private Object[] args;
    private Throwable throwable;
    private long timestamp;
    private String threadName;
    private Map<String, String> mdc;

    public LogEvent(String loggerName, LogLevel level, String message, 
                   Object[] args, Throwable throwable, long timestamp,
                   String threadName, Map<String, String> mdc) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
        this.args = args;
        this.throwable = throwable;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.mdc = mdc;
    }

    // Getters and setters
    public String getLoggerName() { return loggerName; }
    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public Object[] getArgs() { return args; }
    public Throwable getThrowable() { return throwable; }
    public long getTimestamp() { return timestamp; }
    public String getThreadName() { return threadName; }
    public Map<String, String> getMdc() { return mdc; }
} 