package com.youpeng.jpowl.alert.exception;

/**
 * 告警异常类
 */
public class AlertException extends RuntimeException {
    
    public AlertException(String message) {
        super(message);
    }
    
    public AlertException(String message, Throwable cause) {
        super(message, cause);
    }
} 