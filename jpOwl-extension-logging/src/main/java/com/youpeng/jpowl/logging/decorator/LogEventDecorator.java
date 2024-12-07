package com.youpeng.jpowl.logging.decorator;

import com.youpeng.jpowl.logging.model.LogEvent;

/**
 * 日志事件装饰器接口
 */
public interface LogEventDecorator {
    /**
     * 装饰日志事件 不抛出异常，因为装饰操作不应该影响核心流程
     * @param event 原始事件
     * @return 装饰后的事件
     */
    LogEvent decorate(LogEvent event);

    // 是否启用装饰器
    default boolean isEnabled() {
        return true;
    }
} 