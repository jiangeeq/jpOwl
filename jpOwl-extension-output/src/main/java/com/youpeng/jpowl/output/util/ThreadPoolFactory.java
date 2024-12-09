package com.youpeng.jpowl.output.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工厂
 */
public class ThreadPoolFactory {
    
    /**
     * 创建批处理线程池
     */
    public static ScheduledExecutorService createBatchProcessorPool(String namePrefix) {
        return new ScheduledThreadPoolExecutor(
            1,
            new ThreadFactory() {
                private final AtomicInteger counter = new AtomicInteger();
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName(namePrefix + "-" + counter.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static ScheduledExecutorService createScheduledPool(String namePrefix) {
        return new ScheduledThreadPoolExecutor(
                1,
                new ThreadFactory() {
                    private final AtomicInteger counter = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(namePrefix + "-" + counter.incrementAndGet());
                        thread.setDaemon(true);
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    
    /**
     * 创建输出源工作线程池
     */
    public static ExecutorService createOutputSourcePool(String namePrefix, int coreSize) {
        return new ThreadPoolExecutor(
            coreSize,
            coreSize * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private final AtomicInteger counter = new AtomicInteger();
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName(namePrefix + "-worker-" + counter.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
} 