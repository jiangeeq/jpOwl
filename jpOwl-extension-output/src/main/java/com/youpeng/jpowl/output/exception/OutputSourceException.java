package com.youpeng.jpowl.output.exception;

import com.youpeng.jpowl.output.core.OutputSourceType;

/**
 * 输出源异常
 */
public class OutputSourceException extends RuntimeException {
    private final OutputSourceType type;
    
    public OutputSourceException(OutputSourceType type, String message) {
        super(message);
        this.type = type;
    }
    
    public OutputSourceException(OutputSourceType type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
    
    public OutputSourceType getType() {
        return type;
    }
} 