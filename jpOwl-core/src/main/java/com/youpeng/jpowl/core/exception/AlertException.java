package com.youpeng.jpowl.core.exception;

/**
 * 告警相关异常
 * 用于处理告警过程中的异常情况
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class AlertException extends JpOwlException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造告警异常
     *
     * @param message 异常信息
     */
    public AlertException(String message) {
        super(message);
    }
    
    /**
     * 构造告警异常
     *
     * @param message 异常信息
     * @param cause 原始异常
     */
    public AlertException(String message, Throwable cause) {
        super(message, cause);
    }
} 