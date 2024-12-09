package com.youpeng.jpowl.output.load;

import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import com.youpeng.jpowl.output.metrics.OutputSourceMetrics;

/**
 * 健康检查器
 */

public class HealthChecker {
    
    /**
     * 执行健康检查
     */
    public static HealthStatus check(OutputSource source) {
        OutputSourceType type = source.getType();
        OutputSourceMetrics metrics = MetricsCollector.getMetrics(type);
        
        return HealthStatus.create()
            .type(type)
            .healthy(isHealthy(metrics))
            .totalWrites(metrics.getTotalWrites().get())
            .successRate(calculateSuccessRate(metrics))
            .averageLatency(metrics.getAverageLatency())
            .build();
    }
    
    private static boolean isHealthy(OutputSourceMetrics metrics) {
        double successRate = calculateSuccessRate(metrics);
        return successRate >= 0.95; // 成功率大于95%认���是健康的
    }
    
    private static double calculateSuccessRate(OutputSourceMetrics metrics) {
        long total = metrics.getTotalWrites().get();
        if (total == 0) {
            return 1.0;
        }
        return (double) metrics.getSuccessWrites().get() / total;
    }

    public static class HealthStatus {
        private OutputSourceType type;
        private boolean healthy;
        private long totalWrites;
        private double successRate;
        private double averageLatency;

        public static HealthStatus create() {
            return new HealthStatus();
        }
        public HealthStatus build() {
            return this;
        }
        public HealthStatus type(OutputSourceType type) {
            this.type = type;
            return this;
        }

        public HealthStatus healthy(boolean healthy) {
            this.healthy = healthy;
            return this;
        }

        public HealthStatus totalWrites(long totalWrites) {
            this.totalWrites = totalWrites;
            return this;
        }

        public HealthStatus successRate(double successRate) {
            this.successRate = successRate;
            return this;
        }

        public HealthStatus averageLatency(double averageLatency) {
            this.averageLatency = averageLatency;
            return this;
        }

        public OutputSourceType getType() {
            return type;
        }

        public void setType(OutputSourceType type) {
            this.type = type;
        }

        public boolean isHealthy() {
            return healthy;
        }

        public void setHealthy(boolean healthy) {
            this.healthy = healthy;
        }

        public long getTotalWrites() {
            return totalWrites;
        }

        public void setTotalWrites(long totalWrites) {
            this.totalWrites = totalWrites;
        }

        public double getSuccessRate() {
            return successRate;
        }

        public void setSuccessRate(double successRate) {
            this.successRate = successRate;
        }

        public double getAverageLatency() {
            return averageLatency;
        }

        public void setAverageLatency(double averageLatency) {
            this.averageLatency = averageLatency;
        }
    }
} 