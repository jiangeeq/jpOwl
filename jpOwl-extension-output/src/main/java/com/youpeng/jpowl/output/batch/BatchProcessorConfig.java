package com.youpeng.jpowl.output.batch;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 批处理器配置
 */

public class BatchProcessorConfig {
    /**
     * 批处理大小
     */
    private int batchSize = 1000;
    
    /**
     * 队列容量
     */
    private int queueCapacity = 10000;
    
    /**
     * 刷新间隔
     */
    private long flushInterval = 5;
    
    /**
     * 时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    
    /**
     * 调度器
     */
    private ScheduledExecutorService scheduler;
    
    public void validate() {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize must be positive");
        }
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("queueCapacity must be positive");
        }
        if (flushInterval <= 0) {
            throw new IllegalArgumentException("flushInterval must be positive");
        }
        if (scheduler == null) {
            throw new IllegalArgumentException("scheduler cannot be null");
        }
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public long getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(long flushInterval) {
        this.flushInterval = flushInterval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }
}