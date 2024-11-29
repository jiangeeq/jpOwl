package com.youpeng.jpowl.model;

public class Event extends MonitorModel {
    private String name;
    private long timestamp;
    private String message;

    public Event(String name, String message) {
        super("","");
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Event[name=%s, timestamp=%d, message=%s]", name, timestamp, message);
    }

    @Override
    public String serialize() {
        return "Event{name='" + getMessage() + "'}";
    }
}
