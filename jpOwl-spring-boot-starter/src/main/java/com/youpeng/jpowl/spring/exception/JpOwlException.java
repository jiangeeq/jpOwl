package com.youpeng.jpowl.spring.exception;

/**
 * JpOwl 自定义异常类
 */
public class JpOwlException extends RuntimeException {
    
    public JpOwlException(String message) {
        super(message);
    }
    
    public JpOwlException(String message, Throwable cause) {
        super(message, cause);
    }
} 