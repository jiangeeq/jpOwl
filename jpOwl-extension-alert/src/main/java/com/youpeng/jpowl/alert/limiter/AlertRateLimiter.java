package com.youpeng.jpowl.alert.limiter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 告警限流器
 * 防止告警风暴，对同类告警进行频率限制
 */
public class AlertRateLimiter {
    private static final Logger logger = LoggerFactory.getLogger(AlertRateLimiter.class);
    private final Cache<String, Integer> alertCache;
    private final int maxCount;
    private final long timeWindowSeconds;
    
    public AlertRateLimiter(int maxCount, long timeWindowSeconds) {
        this.maxCount = maxCount;
        this.timeWindowSeconds = timeWindowSeconds;
        this.alertCache = CacheBuilder.newBuilder()
            .expireAfterWrite(timeWindowSeconds, TimeUnit.SECONDS)
            .build();
    }
    
    /**
     * 检查是否允许发送告警
     * @param key 告警标识
     * @return 是否允许发送
     */
    public boolean allowAlert(String key) {
        Integer count = alertCache.getIfPresent(key);
        if (count == null) {
            alertCache.put(key, 1);
            return true;
        }
        
        if (count >= maxCount) {
            logger.warn("告警频率超限: key={}, count={}, window={}s", key, count, timeWindowSeconds);
            return false;
        }
        
        alertCache.put(key, count + 1);
        return true;
    }
} 