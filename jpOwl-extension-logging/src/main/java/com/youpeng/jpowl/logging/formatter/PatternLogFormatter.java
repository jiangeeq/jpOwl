package com.youpeng.jpowl.logging.formatter;

import com.youpeng.jpowl.logging.model.LogEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模式化日志格式化器
 * 支持自定义模式的日志格式化
 */
public class PatternLogFormatter implements LogFormatter {
    private final String pattern;
    private final DateTimeFormatter dateFormatter;
    
    public PatternLogFormatter(String pattern) {
        this.pattern = pattern;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    }
    
    @Override
    public String format(LogEvent event) {
        return pattern
            .replace("%d", formatTimestamp(event.getTimestamp()))
            .replace("%p", event.getLevel().toString())
            .replace("%t", event.getThreadName())
            .replace("%c", event.getLoggerName())
            .replace("%m", event.getMessage())
            .replace("%ex", formatException(event.getThrowable()))
            .replace("%X", formatMDC(event.getMdc()));
    }
    
    private String formatTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        ).format(dateFormatter);
    }
    
    private String formatException(Throwable throwable) {
        if (throwable == null) return "";
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return "\n" + sw.toString();
    }
    
    private String formatMDC(Map<String, String> mdc) {
        if (mdc == null || mdc.isEmpty()) return "";
        return mdc.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(", ", "{", "}"));
    }
} 