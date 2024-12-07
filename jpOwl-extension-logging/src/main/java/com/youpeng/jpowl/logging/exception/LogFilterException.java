package com.youpeng.jpowl.logging.exception;

/**
 * 日志过滤异常
 */
public class LogFilterException extends LogException {
    public LogFilterException(String message) {
        super(message);
    }
    
    public LogFilterException(String message, Throwable cause) {
        super(message, cause);
    }
} 