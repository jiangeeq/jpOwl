package com.youpeng.jpowl.logging.model;


import java.util.HashMap;
import java.util.Map;

/**
 * 日志事件构建器
 * 用于构建日志事件对象
 */
public class LogEventBuilder {
    private String loggerName;
    private LogLevel level;
    private String message;
    private Object[] args;
    private Throwable throwable;
    private Map<String, String> context;

    // 私有构造函数，防止外部直接实例化
    private LogEventBuilder() {
        this.context = new HashMap<>();
    }

    // 静态工厂方法
    public static LogEventBuilder create() {
        return new LogEventBuilder();
    }


    // 链式Builder方法
    public LogEventBuilder loggerName(String loggerName) {
        this.loggerName = loggerName;
        return this;
    }

    public LogEventBuilder level(LogLevel level) {
        this.level = level;
        return this;
    }

    public LogEventBuilder message(String message) {
        this.message = message;
        return this;
    }

    public LogEventBuilder args(Object... args) {
        this.args = args;
        return this;
    }

    public LogEventBuilder throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public LogEventBuilder context(String key, String value) {
        this.context.put(key, value);
        return this;
    }

    // 构建最终的LogEvent对象
    public LogEvent build() {
        // 这里假设有一个LogEvent类
        return new LogEvent(loggerName, level, message, args, throwable, context);
    }
}