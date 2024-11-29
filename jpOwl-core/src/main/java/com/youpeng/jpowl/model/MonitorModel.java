package com.youpeng.jpowl.model;

public abstract class MonitorModel {
    public long timestamp;
    public String message;
    public String prefix;

    public MonitorModel(String message, String prefix) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.prefix = prefix;
    }

    public MonitorModel(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
    public String getPrefix() {
        return prefix;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public abstract String serialize();
}
