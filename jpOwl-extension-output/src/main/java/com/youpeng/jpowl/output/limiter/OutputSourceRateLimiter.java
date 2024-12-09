package com.youpeng.jpowl.output.limiter;

import com.youpeng.jpowl.output.core.OutputSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 输出源限流器
 */
public class OutputSourceRateLimiter {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceRateLimiter.class);

    private static final Map<OutputSourceType, RateLimiterInfo> limiterMap = new ConcurrentHashMap<>();
    
    /**
     * 尝试获取令牌
     */
    public static boolean tryAcquire(OutputSourceType type) {
        RateLimiterInfo info = limiterMap.get(type);
        if (info == null) {
            return true;
        }
        
        int currentCount = info.getCurrentCount().get();
        if (currentCount >= info.getMaxPermits()) {
            return false;
        }
        
        return info.getCurrentCount().incrementAndGet() <= info.getMaxPermits();
    }
    
    /**
     * 配置限流
     */
    public static void configure(OutputSourceType type, int maxPermits, long resetIntervalMs) {
        limiterMap.put(type, new RateLimiterInfo(maxPermits, resetIntervalMs));
        
        // 启动重置线程
        startResetThread(type);
    }
    
    private static void startResetThread(OutputSourceType type) {
        Thread resetThread = new Thread(() -> {
            RateLimiterInfo info = limiterMap.get(type);
            while (true) {
                try {
                    Thread.sleep(info.getResetIntervalMs());
                    info.getCurrentCount().set(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        resetThread.setDaemon(true);
        resetThread.setName("rate-limiter-" + type);
        resetThread.start();
    }
    

    private static class RateLimiterInfo {
        private final int maxPermits;
        private final long resetIntervalMs;
        private final AtomicInteger currentCount = new AtomicInteger(0);

        public RateLimiterInfo(int maxPermits, long resetIntervalMs) {
            this.maxPermits = maxPermits;
            this.resetIntervalMs = resetIntervalMs;
        }

        public int getMaxPermits() {
            return maxPermits;
        }

        public long getResetIntervalMs() {
            return resetIntervalMs;
        }

        public AtomicInteger getCurrentCount() {
            return currentCount;
        }
    }
} 