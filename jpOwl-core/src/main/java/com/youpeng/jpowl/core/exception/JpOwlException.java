package com.youpeng.jpowl.core.exception;

/**
 * JPOwl基础异常类
 * 框架中所有自定义异常的父类
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class JpOwlException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造基础异常
     *
     * @param message 异常信息
     */
    public JpOwlException(String message) {
        super(message);
    }
    
    /**
     * 构造基础异常
     *
     * @param message 异常信息
     * @param cause 原始异常
     */
    public JpOwlException(String message, Throwable cause) {
        super(message, cause);
    }
} 