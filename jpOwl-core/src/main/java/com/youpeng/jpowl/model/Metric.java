package com.youpeng.jpowl.model;


public class Metric extends MonitorModel {
    private String name;
    private long count;
    private double sum;
    private double average;

    public Metric(String name) {
        super("","");
        this.name = name;
        this.timestamp = System.currentTimeMillis();
        this.count = 0;
        this.sum = 0;
        this.average = 0;
    }

    public synchronized  void record(double value) {
        this.count++;
        this.sum += value;
        this.average = this.sum / this.count;
    }

    public synchronized void reset() {
        this.count = 0;
        this.sum = 0;
        this.average = 0;
    }


    @Override
    public String toString() {
        return String.format("Metric[name=%s, timestamp=%d, count=%d, sum=%.2f, average=%.2f]",
                name, timestamp, count, sum, average);
    }

    @Override
    public String serialize() {
        return "Metric{name='" + getMessage() + "', value=" + sum + "}";
    }
}
