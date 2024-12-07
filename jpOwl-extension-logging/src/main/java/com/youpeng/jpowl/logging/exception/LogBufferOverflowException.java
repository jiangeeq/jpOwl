package com.youpeng.jpowl.logging.exception;

/**
 * 日志缓冲区溢出异常
 */
public class LogBufferOverflowException extends LogException {
    private final int bufferSize;
    private final int attemptedSize;
    
    public LogBufferOverflowException(String message, int bufferSize, int attemptedSize) {
        super(message);
        this.bufferSize = bufferSize;
        this.attemptedSize = attemptedSize;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public int getAttemptedSize() {
        return attemptedSize;
    }
} 