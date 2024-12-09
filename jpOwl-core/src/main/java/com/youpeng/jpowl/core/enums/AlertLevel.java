package com.youpeng.jpowl.core.enums;

/**
 * 告警级别枚举
 */
public enum AlertLevel {
    INFO,
    WARNING,
    ERROR,
    CRITICAL;
    
    public boolean isHigherThan(AlertLevel other) {
        return this.ordinal() > other.ordinal();
    }
} 