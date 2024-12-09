package com.youpeng.jpowl.output.metrics;

import com.youpeng.jpowl.output.core.OutputSourceType;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 输出源监控指标
 */
public class OutputSourceMetrics {
    private final OutputSourceType type;
    private final AtomicLong totalWrites = new AtomicLong();
    private final AtomicLong successWrites = new AtomicLong();
    private final AtomicLong failedWrites = new AtomicLong();
    private final AtomicLong totalLatency = new AtomicLong();
    
    public OutputSourceMetrics(OutputSourceType type) {
        this.type = type;
    }
    
    /**
     * 记录写入操作
     */
    public void recordWrite(boolean success, long latencyMs) {
        totalWrites.incrementAndGet();
        totalLatency.addAndGet(latencyMs);
        if (success) {
            successWrites.incrementAndGet();
        } else {
            failedWrites.incrementAndGet();
        }
    }
    
    /**
     * 获取平均延迟
     */
    public double getAverageLatency() {
        long total = totalWrites.get();
        return total == 0 ? 0 : (double) totalLatency.get() / total;
    }

    public OutputSourceType getType() {
        return type;
    }

    public AtomicLong getTotalWrites() {
        return totalWrites;
    }

    public AtomicLong getSuccessWrites() {
        return successWrites;
    }

    public AtomicLong getFailedWrites() {
        return failedWrites;
    }

    public AtomicLong getTotalLatency() {
        return totalLatency;
    }
}