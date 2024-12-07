package com.youpeng.jpowl.logging.handler;
// 一些具体的处理器示例：

import com.youpeng.jpowl.logging.model.LogEvent;

/**
 * MDC（Mapped Diagnostic Context）处理器
 * 用于添加上下文信息
 */
public class MDCHandler implements LogEventHandler {
    @Override
    public LogEvent handle(LogEvent event) {
        // 添加线程上下文信息
        event.addMdc("threadId", Thread.currentThread().getId());
        event.addMdc("classLoader", Thread.currentThread().getContextClassLoader().toString());
        return event;
    }
    
    @Override
    public String getName() {
        return "MDCHandler";
    }
    
    @Override
    public int getOrder() {
        return 100; // 较高优先级，确保上下文信��早期被添加
    }
}