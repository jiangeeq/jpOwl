package com.youpeng.jpowl.logging.handler;

import com.youpeng.jpowl.logging.exception.LogHandlerException;
import com.youpeng.jpowl.logging.model.LogEvent;

/**
 * 日志事件处理器接口
 */
public interface LogEventHandler {
    /**
     * 处理日志事件
     * @return 处理后的事件，如果返回null表示终止处理链
     */
    LogEvent handle(LogEvent event) throws LogHandlerException;

    /**
     * 获取处理器名称
     */
    String getName();

    /**
     * 处理器优先级，数字越小优先级越高
     */
    default int getOrder() {
        return Integer.MAX_VALUE;
    }
}

