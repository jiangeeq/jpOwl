package com.youpeng.jpowl.logging.process;

import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 批处理日志处理器
 * 支持日志的批量处理和刷新
 */
public class BatchLogProcessor {
    private final Queue<LogEvent> buffer;
    private final int batchSize;
    private final long flushInterval;
    private final LogProcessingChain processingChain;
    private final ScheduledExecutorService scheduler;
    
    public BatchLogProcessor(LogProcessingChain processingChain, int batchSize, long flushInterval) {
        this.processingChain = processingChain;
        this.batchSize = batchSize;
        this.flushInterval = flushInterval;
        this.buffer = new ConcurrentLinkedQueue<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // 定时刷新
        scheduler.scheduleAtFixedRate(
            this::flush,
            flushInterval,
            flushInterval,
            TimeUnit.MILLISECONDS
        );
    }
    
    /**
     * 添加日志事件到缓冲区
     */
    public void process(LogEvent event) {
        buffer.offer(event);
        if (buffer.size() >= batchSize) {
            flush();
        }
    }
    
    /**
     * 强制刷新缓冲区
     */
    public synchronized void flush() {
        List<LogEvent> batch = new ArrayList<>();
        LogEvent event;
        while ((event = buffer.poll()) != null) {
            batch.add(event);
        }
        
        if (!batch.isEmpty()) {
            processBatch(batch);
        }
    }
    
    private void processBatch(List<LogEvent> batch) {
        try {
            for (LogEvent event : batch) {
                processingChain.process(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void shutdown() {
        scheduler.shutdown();
        flush(); // 最后一次刷新
    }
} 