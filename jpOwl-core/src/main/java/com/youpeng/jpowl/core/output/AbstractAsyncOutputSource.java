package com.youpeng.jpowl.core.output;

import com.youpeng.jpowl.core.model.MonitorEvent;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步输出源抽象基类
 */
public abstract class AbstractAsyncOutputSource extends AbstractOutputSource {
    protected final BlockingQueue<MonitorEvent> queue;
    protected final Thread worker;
    protected final AtomicBoolean running = new AtomicBoolean(true);
    
    protected AbstractAsyncOutputSource(Map<String, Object> properties) {
        super(properties);
        int queueSize = getProperty("queue.size", 10000);
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.worker = new Thread(this::processQueue, "async-output-worker");
        this.worker.setDaemon(true);
        this.worker.start();
    }
    
    @Override
    public void write(MonitorEvent event) {
        if (!running.get()) {
            throw new IllegalStateException("Output source is closed");
        }
        
        try {
            if (!queue.offer(event, 1, TimeUnit.SECONDS)) {
                logger.warn("Queue full, event dropped");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while queueing event", e);
        }
    }
    
    private void processQueue() {
        while (running.get() || !queue.isEmpty()) {
            try {
                MonitorEvent event = queue.poll(100, TimeUnit.MILLISECONDS);
                if (event != null) {
                    doWrite(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.error("Error processing event", e);
            }
        }
    }
    
    @Override
    public void close() {
        running.set(false);
        try {
            worker.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 实际的写入操作
     */
    protected abstract void doWrite(MonitorEvent event);
} 