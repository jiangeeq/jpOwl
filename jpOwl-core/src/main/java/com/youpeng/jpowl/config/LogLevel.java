package com.youpeng.jpowl.config;

public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR;

    public boolean isEnabled(LogLevel currentLevel) {
        return this.ordinal() >= currentLevel.ordinal();
    }
}
