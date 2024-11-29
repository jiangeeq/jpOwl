package com.youpeng.jpowl.model;

import java.util.ArrayList;
import java.util.List;

public class Transaction extends MonitorModel {
    private String name;
    private long startTime;
    private long endTime;
    private int count;
    private List<Event> events;

    public Transaction(String name) {
        super("","");
        this.name = name;
        this.startTime = System.currentTimeMillis();
        this.count = 0;
        this.events = new ArrayList<>();
    }

    public void complete() {
        this.endTime = System.currentTimeMillis();
        this.count++;
    }

    public long getDuration() {
        return this.endTime - this.startTime;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }
    @Override
    public String toString() {
        return String.format("Transaction[name=%s, duration=%dms, count=%d, events=%s]",
                name, getDuration(), count, events);
    }
    @Override
    public String serialize() {
        return "Transaction{name='" + getMessage() + "'";
    }
}
