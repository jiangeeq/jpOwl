package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.manager.OutputSourceManager;
import com.youpeng.jpowl.output.manager.OutputSourceStatusManager;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import com.youpeng.jpowl.output.metrics.OutputSourceMetrics;
import com.youpeng.jpowl.output.model.OutputSourceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * 输出源监控器
 */
public class OutputSourceMonitor {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceManager.class);

    /**
     * 获取监控信息
     */
    public static MonitorInfo getMonitorInfo(OutputSourceType type) {
        OutputSourceMetrics metrics = MetricsCollector.getMetrics(type);
        OutputSourceStatus status = OutputSourceStatusManager.getStatus(type);
        
        return MonitorInfo.create()
            .type(type)
            .status(status.getStatus())
            .totalWrites(metrics.getTotalWrites().get())
            .successWrites(metrics.getSuccessWrites().get())
            .failedWrites(metrics.getFailedWrites().get())
            .averageLatency(metrics.getAverageLatency())
            .lastUpdatedTime(status.getLastUpdatedTime())
            .lastError(status.getLastError())
            .build();
    }

    public static class MonitorInfo {
    private OutputSourceType type;
    private OutputSourceStatus.Status status;
    private long totalWrites;
    private long successWrites;
    private long failedWrites;
    private double averageLatency;
    private Instant lastUpdatedTime;
    private String lastError;
    public static MonitorInfo create(){
        return new MonitorInfo();
    }
    public MonitorInfo build(){
        return this;
    }
    public MonitorInfo type(OutputSourceType type) {
        this.type = type;
        return this;
    }

    public MonitorInfo status(OutputSourceStatus.Status status) {
        this.status = status;
        return this;
    }

    public MonitorInfo totalWrites(long totalWrites) {
        this.totalWrites = totalWrites;
        return this;
    }

    public MonitorInfo successWrites(long successWrites) {
        this.successWrites = successWrites;
        return this;
    }

    public MonitorInfo failedWrites(long failedWrites) {
        this.failedWrites = failedWrites;
        return this;
    }

    public MonitorInfo averageLatency(double averageLatency) {
        this.averageLatency = averageLatency;
        return this;
    }

    public MonitorInfo lastUpdatedTime(Instant lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
        return this;
    }

    public MonitorInfo lastError(String lastError) {
        this.lastError = lastError;
        return this;
    }
}

} 