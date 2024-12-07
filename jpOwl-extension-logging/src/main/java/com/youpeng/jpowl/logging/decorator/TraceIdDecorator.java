package com.youpeng.jpowl.logging.decorator;

import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 追踪ID装饰器
 * 为日志事件添加追踪ID
 */
public class TraceIdDecorator implements LogEventDecorator {
    private final String traceIdKey;
    private final Supplier<String> traceIdGenerator;
    
    public TraceIdDecorator(String traceIdKey, Supplier<String> traceIdGenerator) {
        this.traceIdKey = traceIdKey;
        this.traceIdGenerator = traceIdGenerator;
    }
    
    @Override
    public LogEvent decorate(LogEvent event) {
        Map<String, String> mdc = new HashMap<>(event.getMdc());
        if (!mdc.containsKey(traceIdKey)) {
            mdc.put(traceIdKey, traceIdGenerator.get());
        }
        LogEvent logEvent = LogEvent.create(event);
        logEvent.setMdc(mdc);
        return logEvent;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}