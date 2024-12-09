package com.youpeng.jpowl.output.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试工具类
 */
public class RetryUtil {
    private static final Logger logger = LoggerFactory.getLogger(RetryUtil.class);

    public static <T> T executeWithRetry(Callable<T> task, int maxRetries, long retryInterval) {
        int retryCount = 0;
        Exception lastException = null;
        
        while (retryCount < maxRetries) {
            try {
                return task.call();
            } catch (Exception e) {
                lastException = e;
                retryCount++;
                
                if (retryCount < maxRetries) {
                    logger.warn("Retry attempt {} of {} failed, will retry in {} ms",
                        retryCount, maxRetries, retryInterval, e);
                    try {
                        TimeUnit.MILLISECONDS.sleep(retryInterval);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }
        
        throw new RuntimeException("Failed after " + maxRetries + " retries", lastException);
    }
} 