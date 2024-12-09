package com.youpeng.jpowl.logging.monitor;


import com.youpeng.jpowl.logging.model.LogLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 指标快照，用于导出指标数据
 */
public class MetricsSnapshot {
    private final long totalLogs;
    private final Map<LogLevel, Long> levelCounts;
    private final long errorCount;
    private final long totalProcessingTime;
    private final long droppedLogs;
    private final double logsPerSecond;

    public MetricsSnapshot(long totalLogs, Map<LogLevel, Long> levelCounts, long errorCount, long totalProcessingTime, long droppedLogs, double logsPerSecond) {
        this.totalLogs = totalLogs;
        this.levelCounts = levelCounts;
        this.errorCount = errorCount;
        this.totalProcessingTime = totalProcessingTime;
        this.droppedLogs = droppedLogs;
        this.logsPerSecond = logsPerSecond;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalLogs", totalLogs);
        metrics.put("levelCounts", levelCounts);
        metrics.put("errorCount", errorCount);
        metrics.put("avgProcessingTime", totalLogs > 0 ?
                totalProcessingTime / totalLogs : 0);
        metrics.put("droppedLogs", droppedLogs);
        metrics.put("logsPerSecond", logsPerSecond);
        return metrics;
    }

    public long getTotalLogs() {
        return totalLogs;
    }

    public long getErrorCount() {
        return errorCount;
    }
}
