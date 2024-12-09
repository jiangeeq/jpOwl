package com.youpeng.jpowl.output.batch;

import com.youpeng.jpowl.output.model.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 批处理器
 * 支持按数量和时间触发批处理
 */
public class BatchProcessor implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(BatchProcessor.class);
    private final BlockingQueue<MonitorData> queue;
    private final Consumer<List<MonitorData>> batchConsumer;
    private final int batchSize;
    private final long flushInterval;
    private final TimeUnit timeUnit;
    private final ScheduledExecutorService scheduler;
    private volatile boolean running = true;

    public BatchProcessor(BatchProcessorConfig config, Consumer<List<MonitorData>> batchConsumer) {
        this.queue = new LinkedBlockingQueue<>(config.getQueueCapacity());
        this.batchConsumer = batchConsumer;
        this.batchSize = config.getBatchSize();
        this.flushInterval = config.getFlushInterval();
        this.timeUnit = config.getTimeUnit();
        this.scheduler = config.getScheduler();
        
        startProcessing();
    }

    private void startProcessing() {
        // 定时刷新
        scheduler.scheduleWithFixedDelay(this::flush, flushInterval, flushInterval, timeUnit);
        
        // 批量处理
        scheduler.submit(() -> {
            while (running) {
                try {
                    processBatch();
                } catch (Exception e) {
                    logger.error("Error processing batch", e);
                }
            }
        });
    }

    private void processBatch() {
        List<MonitorData> batch = new ArrayList<>(batchSize);
        queue.drainTo(batch, batchSize);
        
        if (!batch.isEmpty()) {
            try {
                batchConsumer.accept(batch);
            } catch (Exception e) {
                logger.error("Error processing batch of {} items", batch.size(), e);
            }
        }
    }

    public void add(MonitorData data) {
        if (!queue.offer(data)) {
            logger.warn("Queue is full, dropping data");
        }
    }

    public void flush() {
        List<MonitorData> batch = new ArrayList<>();
        queue.drainTo(batch);
        if (!batch.isEmpty()) {
            try {
                batchConsumer.accept(batch);
            } catch (Exception e) {
                logger.error("Error flushing batch of {} items", batch.size(), e);
            }
        }
    }

    @Override
    public void close() {
        running = false;
        flush();
    }
} 