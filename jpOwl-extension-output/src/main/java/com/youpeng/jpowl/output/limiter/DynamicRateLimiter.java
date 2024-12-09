package com.youpeng.jpowl.output.limiter;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.load.LoadDetectorFactory;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import com.youpeng.jpowl.output.metrics.OutputSourceMetrics;
import com.youpeng.jpowl.output.model.LoadStatus;
import com.youpeng.jpowl.output.util.ThreadPoolFactory;
import com.youpeng.jpowl.output.load.LoadDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态限流器
 * 根据输出源的负载情况动态调整限流策略
 */
public class DynamicRateLimiter {
    private static final Logger logger = LoggerFactory.getLogger(DynamicRateLimiter.class);

    private static final Map<OutputSourceType, LimiterContext> contextMap = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = ThreadPoolFactory.createScheduledPool("dynamic-rate-limiter");

    public DynamicRateLimiter(OutputSourceType outputSourceType, LimiterConfig config) {
        configure(outputSourceType, config);
    }

    /**
     * 尝试获取令牌
     */
    public static boolean tryAcquire(OutputSourceType type) {
        LimiterContext context = contextMap.get(type);
        if (context == null) {
            return true;
        }

        int currentQps = context.getCurrentQps().get();
        int currentCount = context.getCurrentCount().incrementAndGet();
        
        if (currentCount > currentQps) {
            context.getCurrentCount().decrementAndGet();
            logger.debug("Rate limit exceeded for {}, current QPS: {}", type, currentQps);
            return false;
        }
        return true;
    }

    /**
     * 配置动态限流
     */
    public static void configure(OutputSourceType type, LimiterConfig config) {
        LimiterContext context = new LimiterContext(type, config);
        contextMap.put(type, context);
        
        // 启动动态调整任务
        startAdjustmentTask(context);
        // 启动计数器置任务
        startResetTask(context);
        
        logger.info("Configured dynamic rate limiter for {} with initial QPS: {}", 
            type, config.getInitialQps());
    }

    private static void startAdjustmentTask(LimiterContext context) {
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(
            () -> adjustRate(context),
            context.getConfig().getCheckIntervalMs(),
            context.getConfig().getCheckIntervalMs(),
            TimeUnit.MILLISECONDS
        );
        context.setAdjustmentTask(future);
    }

    private static void startResetTask(LimiterContext context) {
        scheduler.scheduleAtFixedRate(
            () -> context.getCurrentCount().set(0),
            1, 1, TimeUnit.SECONDS
        );
    }

    static void adjustRate(LimiterContext context) {
        try {
            OutputSourceMetrics metrics = MetricsCollector.getMetrics(context.getType());
            LoadDetector loadDetector = LoadDetectorFactory.getDetector(context.getType());
            LoadStatus loadStatus = loadDetector.detect();
            LimiterConfig config = context.getConfig();
            
            double successRate = calculateSuccessRate(metrics);
            double avgLatency = metrics.getAverageLatency();
            
            int currentQps = context.getCurrentQps().get();
            int newQps = currentQps;

            // 根据负载状态调整QPS
            if (loadStatus.isOverloaded()) {
                // 系统过载，大幅降低QPS
                newQps = Math.max(currentQps / 2, config.getMinQps());
                logger.warn("System overloaded, reducing QPS significantly for {}", context.getType());
            } else if (loadStatus.getCpuUsage() > 70 || loadStatus.getMemoryUsage() > 80) {
                // 负载较高，适当降低QPS
                newQps = Math.max(currentQps - config.getAdjustmentStep() * 2, config.getMinQps());
            } else if (successRate >= config.getSuccessRateThreshold() 
                && avgLatency <= config.getLatencyThreshold()
                && loadStatus.getCpuUsage() < 50
                && loadStatus.getMemoryUsage() < 60) {
                // 系统状态良好，可以适当增加QPS
                newQps = Math.min(currentQps + config.getAdjustmentStep(), config.getMaxQps());
            }

            if (newQps != currentQps) {
                context.getCurrentQps().set(newQps);
                logger.info("Adjusted QPS for {} from {} to {}, load status: CPU {}%, Memory {}%, IO {}%",
                    context.getType(), currentQps, newQps, 
                    loadStatus.getCpuUsage(), loadStatus.getMemoryUsage(), loadStatus.getDiskIoUsage());
            }
        } catch (Exception e) {
            logger.error("Error adjusting rate for {}", context.getType(), e);
        }
    }

    private static double calculateSuccessRate(OutputSourceMetrics metrics) {
        long total = metrics.getTotalWrites().get();
        if (total == 0) {
            return 1.0;
        }
        return (double) metrics.getSuccessWrites().get() / total;
    }

    /**
     * 获取限流器上下文
     */
    public static LimiterContext getContext(OutputSourceType type) {
        return contextMap.get(type);
    }

    /**
     * 验证配置
     */
    private static void validateConfig(LimiterConfig config) {
        if (config.getInitialQps() <= 0) {
            throw new IllegalArgumentException("Initial QPS must be positive");
        }
        if (config.getMaxQps() < config.getInitialQps()) {
            throw new IllegalArgumentException("Max QPS must be greater than or equal to initial QPS");
        }
        if (config.getMinQps() <= 0 || config.getMinQps() > config.getInitialQps()) {
            throw new IllegalArgumentException("Min QPS must be positive and less than or equal to initial QPS");
        }
        if (config.getAdjustmentStep() <= 0) {
            throw new IllegalArgumentException("Adjustment step must be positive");
        }
        if (config.getSuccessRateThreshold() <= 0 || config.getSuccessRateThreshold() > 1) {
            throw new IllegalArgumentException("Success rate threshold must be between 0 and 1");
        }
        if (config.getLatencyThreshold() <= 0) {
            throw new IllegalArgumentException("Latency threshold must be positive");
        }
    }

    public static class LimiterConfig {
        private int initialQps = 1000;
        private int minQps = 100;
        private int maxQps = 5000;
        private long checkIntervalMs = 5000;
        private double successRateThreshold = 0.95;
        private long latencyThreshold = 100;
        private int adjustmentStep = 100;

        public int getInitialQps() {
            return initialQps;
        }

        public void setInitialQps(int initialQps) {
            this.initialQps = initialQps;
        }

        public int getMinQps() {
            return minQps;
        }

        public void setMinQps(int minQps) {
            this.minQps = minQps;
        }

        public int getMaxQps() {
            return maxQps;
        }

        public void setMaxQps(int maxQps) {
            this.maxQps = maxQps;
        }

        public long getCheckIntervalMs() {
            return checkIntervalMs;
        }

        public void setCheckIntervalMs(long checkIntervalMs) {
            this.checkIntervalMs = checkIntervalMs;
        }

        public double getSuccessRateThreshold() {
            return successRateThreshold;
        }

        public void setSuccessRateThreshold(double successRateThreshold) {
            this.successRateThreshold = successRateThreshold;
        }

        public long getLatencyThreshold() {
            return latencyThreshold;
        }

        public void setLatencyThreshold(long latencyThreshold) {
            this.latencyThreshold = latencyThreshold;
        }

        public int getAdjustmentStep() {
            return adjustmentStep;
        }

        public void setAdjustmentStep(int adjustmentStep) {
            this.adjustmentStep = adjustmentStep;
        }
    }
    
    public static class LimiterContext {
        private final OutputSourceType type;
        private final LimiterConfig config;
        private final AtomicInteger currentQps;
        private final AtomicInteger currentCount = new AtomicInteger(0);
        private ScheduledFuture<?> adjustmentTask;

        public LimiterContext(OutputSourceType type, LimiterConfig config) {
            this.type = type;
            this.config = config;
            this.currentQps = new AtomicInteger(config.getInitialQps());
        }

        public OutputSourceType getType() {
            return type;
        }

        public LimiterConfig getConfig() {
            return config;
        }

        public AtomicInteger getCurrentQps() {
            return currentQps;
        }

        public AtomicInteger getCurrentCount() {
            return currentCount;
        }

        public ScheduledFuture<?> getAdjustmentTask() {
            return adjustmentTask;
        }

        public void setAdjustmentTask(ScheduledFuture<?> adjustmentTask) {
            this.adjustmentTask = adjustmentTask;
        }
    }
} 