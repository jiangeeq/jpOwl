package com.youpeng.jpowl.output.manager;

import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.core.OutputSourceFactory;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.model.MonitorData;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 输出源管理器
 * 负责管理所有输出源的生命周期
 */
public class OutputSourceManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceManager.class);
    private final Map<OutputSourceType, OutputSource> outputSources = new ConcurrentHashMap<>();
    private final ExecutorService executorService;
    // 进行限流检查
//    OutputSourceRateLimiter
    /**
     * 构造函数用于初始化OutputSourceManager类
     * 它创建了一个线程池来处理输出源的异步任务
     */
    public OutputSourceManager() {
        // 初始化一个线程池，用于执行提交的任务
        this.executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            // 使用AtomicInteger来为新创建的线程生成唯一的序号
            private final AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                // 创建一个新的线程实例
                Thread thread = new Thread(r);
                // 为线程命名，以便于调试和日志记录
                thread.setName("output-source-" + counter.incrementAndGet());
                // 设置线程为守护线程，这样当主线程结束时，守护线程会自动退出
                thread.setDaemon(true);
                // 返回创建的线程实例
                return thread;
            }
        });
    }

    /**
     * 注册输出源
     */
    public void register(OutputSourceType type, Map<String, Object> properties) {
        OutputSource outputSource = OutputSourceFactory.createOutputSource(type, properties);
        outputSources.put(type, outputSource);
        logger.info("Registered output source: {}", type);
    }

    /**
     * 写入数据到所有输出源
     */
    public void write(MonitorData data) {
        outputSources.values().forEach(source ->
                executorService.submit(() -> {
                    try {
                        long startTime = System.currentTimeMillis();
                        source.write(data);
                        long duration = System.currentTimeMillis() - startTime;
                        MetricsCollector.recordWrite(source.getType(), true, duration);
                    } catch (Exception e) {
                        MetricsCollector.recordWrite(source.getType(), false, 0);
                        logger.error("Failed to write data to output source", e);
                    }
                })
        );
    }

    /**
     * 批量写入数据到所有输出源
     */
    public void writeBatch(List<MonitorData> dataList) {
        outputSources.values().forEach(source ->
                executorService.submit(() -> {
                    try {
                        source.writeBatch(dataList);
                    } catch (Exception e) {
                        logger.error("Failed to write batch data to output source", e);
                    }
                })
        );
    }

    @Override
    public void close() {
        try {
            // 等待所有任务完成
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            outputSources.values().forEach(source -> {
                try {
                    source.close();
                } catch (Exception e) {
                    logger.error("Failed to close output source", e);
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 清理资源
            outputSources.clear();
        }
    }
} 