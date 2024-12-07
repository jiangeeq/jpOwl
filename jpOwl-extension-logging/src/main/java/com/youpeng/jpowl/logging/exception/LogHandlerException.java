package com.youpeng.jpowl.logging.exception;

/**
 * 日志处理器异常
 */
public class LogHandlerException extends LogException {
    
    public LogHandlerException(String message) {
        super(message);
    }
    
    public LogHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LogHandlerException(Throwable cause) {
        super(cause);
    }
} 