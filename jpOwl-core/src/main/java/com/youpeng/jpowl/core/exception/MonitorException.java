package com.youpeng.jpowl.core.exception;

/**
 * 监控相关异常
 * 用于处理监控过程中的异常情况
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MonitorException extends JpOwlException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造监控异常
     *
     * @param message 异常信息
     */
    public MonitorException(String message) {
        super(message);
    }
    
    /**
     * 构造监控异常
     *
     * @param message 异常信息
     * @param cause 原始异常
     */
    public MonitorException(String message, Throwable cause) {
        super(message, cause);
    }
} 