package com.youpeng.jpowl.core.buffer;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.exception.MonitorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 监控事件缓冲区
 */
public class MonitorEventBuffer {
    private final BlockingQueue<MonitorEvent> buffer;
    private final int capacity;
    private final long timeout;
    
    public MonitorEventBuffer(int capacity, long timeoutMs) {
        this.capacity = capacity;
        this.timeout = timeoutMs;
        this.buffer = new ArrayBlockingQueue<>(capacity);
    }
    
    public void put(MonitorEvent event) throws MonitorException {
        try {
            if (!buffer.offer(event, timeout, TimeUnit.MILLISECONDS)) {
                throw new MonitorException("Buffer full, event dropped");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MonitorException("Interrupted while buffering event", e);
        }
    }
    
    public List<MonitorEvent> drain() {
        List<MonitorEvent> events = new ArrayList<>();
        buffer.drainTo(events);
        return events;
    }
    
    public List<MonitorEvent> drain(int maxElements) {
        List<MonitorEvent> events = new ArrayList<>();
        buffer.drainTo(events, maxElements);
        return events;
    }
    
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
    
    public int size() {
        return buffer.size();
    }
    
    public int remainingCapacity() {
        return buffer.remainingCapacity();
    }
} 