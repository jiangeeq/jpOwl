package com.youpeng.jpowl.logging.model;


import org.slf4j.helpers.MessageFormatter;

import java.time.Instant;
import java.util.Map;

/**
 * LogEvent类用于封装日志事件的相关信息
 * 它包含了日志记录所需的各种数据，如日志器名称、日志级别、消息内容等
 */
public class LogEvent {
    // 日志器名称，用于标识日志记录器
    private String loggerName;
    // 日志级别，表示日志的重要程度
    private LogLevel level;
    // 日志消息，记录日志的主要内容
    private String message;
    // 消息参数，用于格式化消息
    private Object[] args;
    // 异常信息，记录发生异常时的对象
    private Throwable throwable;
    // 时间戳，记录日志事件发生的时间
    private long timestamp;
    // 线程名称，记录生成日志时的线程名
    private String threadName;
    // MDC (Mapped Diagnostic Context) 信息，用于在日志中添加额外的上下文信息
    private Map<String, String> mdc;

    public LogEvent() {
    }

    public LogEvent(String loggerName, LogLevel level, String message, Object[] args, Throwable throwable, long timestamp, String threadName, Map<String, String> mdc) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
        this.args = args;
        this.throwable = throwable;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.mdc = mdc;
    }
    public LogEvent(String loggerName, LogLevel level, String message, Object[] args, Throwable throwable, Map<String, String> mdc) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
        this.args = args;
        this.throwable = throwable;
        this.timestamp = Instant.now().toEpochMilli();
        this.threadName = Thread.currentThread().getName();;
        this.mdc = mdc;
    }

    /**
     * 格式化消息方法
     * 如果存在参数，则使用参数格式化消息；否则直接返回消息内容
     *
     * @return 格式化后的消息内容
     */
    public String formatMessage() {
        if (args != null && args.length > 0) {
            // 使用MessageFormatter格式化消息，当消息中包含占位符时，使用参数进行替换
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        // 如果没有参数，直接返回消息内容
        return message;
    }

    public String getMessage() {
        return formatMessage();
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Map<String, String> getMdc() {
        return mdc;
    }

    public void setMdc(Map<String, String> mdc) {
        this.mdc = mdc;
    }

    public void copyFrom(LogEvent source) {
       this.loggerName = source.getLoggerName();
       this.level = source.getLevel();
       this.message = source.getMessage();
       this.args = source.getArgs();
       this.throwable = source.getThrowable();
       this.timestamp = source.getTimestamp();
       this.threadName = source.getThreadName();
       this.mdc = source.getMdc();

    }

    public LogEvent addMdc(Object key, Object value) {
        this.mdc.put(key.toString(), value.toString());
        return this;
    }

    public static LogEvent create(LogEvent source) {
        return new LogEvent(source.getLoggerName(), source.getLevel(), source.getMessage(), source.getArgs(), source.getThrowable(), source.getTimestamp(), source.getThreadName(), source.getMdc());
    }
}
