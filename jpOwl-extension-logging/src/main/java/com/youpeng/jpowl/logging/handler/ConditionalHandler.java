package com.youpeng.jpowl.logging.handler;

import com.youpeng.jpowl.logging.exception.LogHandlerException;
import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.function.Predicate;

/**
 * 条件处理器
 */
public class ConditionalHandler implements LogEventHandler {
    private final Predicate<LogEvent> condition;
    private final LogEventHandler handler;

    public ConditionalHandler(Predicate<LogEvent> condition, LogEventHandler handler) {
        this.condition = condition;
        this.handler = handler;
    }

    @Override
    public LogEvent handle(LogEvent event) throws LogHandlerException {
        if (condition.test(event)) {
            return handler.handle(event);
        }
        return event;
    }

    @Override
    public String getName() {
        return "ConditionalHandler";
    }

    @Override
    public int getOrder() {
        return 299;
    }
}