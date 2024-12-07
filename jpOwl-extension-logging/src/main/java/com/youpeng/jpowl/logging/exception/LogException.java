package com.youpeng.jpowl.logging.exception;

/**
 * 日志基础异常类
 */
public class LogException extends Exception {
    public LogException(String message) {
        super(message);
    }
    
    public LogException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LogException(Throwable cause) {
        super(cause);
    }
} 