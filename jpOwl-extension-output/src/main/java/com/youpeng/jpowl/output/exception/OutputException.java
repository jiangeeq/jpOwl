package com.youpeng.jpowl.output.exception;

/**
 * 输出操作异常类
 */
public class OutputException extends RuntimeException {
    public OutputException(String message) {
        super(message);
    }
    
    public OutputException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OutputException(Throwable cause) {
        super(cause);
    }
} 