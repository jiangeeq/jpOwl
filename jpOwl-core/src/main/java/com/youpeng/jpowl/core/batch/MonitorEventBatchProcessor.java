package com.youpeng.jpowl.core.batch;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.buffer.MonitorEventBuffer;
import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.metrics.MetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 监控事件批处理器
 */
public class MonitorEventBatchProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MonitorEventBatchProcessor.class);
    
    private final MonitorEventBuffer buffer;
    private final OutputSource outputSource;
    private final ScheduledExecutorService scheduler;
    private final int batchSize;
    private final long flushInterval;
    private ScheduledFuture<?> flushTask;
    
    public MonitorEventBatchProcessor(MonitorEventBuffer buffer, 
                                    OutputSource outputSource,
                                    ScheduledExecutorService scheduler,
                                    int batchSize,
                                    long flushIntervalMs) {
        this.buffer = buffer;
        this.outputSource = outputSource;
        this.scheduler = scheduler;
        this.batchSize = batchSize;
        this.flushInterval = flushIntervalMs;
    }
    
    public void start() {
        flushTask = scheduler.scheduleWithFixedDelay(
            this::flush,
            flushInterval,
            flushInterval,
            TimeUnit.MILLISECONDS
        );
    }
    
    public void stop() {
        if (flushTask != null) {
            flushTask.cancel(false);
            flush(); // 最后一次刷新
        }
    }
    
    private void flush() {
        try {
            List<MonitorEvent> batch = buffer.drain(batchSize);
            if (!batch.isEmpty()) {
                processBatch(batch);
            }
        } catch (Exception e) {
            logger.error("Failed to flush monitor events", e);
            MetricsCollector.incrementCounter("monitor.batch.errors");
        }
    }
    
    private void processBatch(List<MonitorEvent> batch) {
        try {
            for (MonitorEvent event : batch) {
                outputSource.write(event);
            }
            MetricsCollector.incrementCounter("monitor.batch.processed");
            MetricsCollector.updateGauge("monitor.batch.size", batch.size());
        } catch (Exception e) {
            logger.error("Failed to process batch", e);
            MetricsCollector.incrementCounter("monitor.batch.errors");
        }
    }
} 