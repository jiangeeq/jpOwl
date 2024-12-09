package com.youpeng.jpowl.core.monitor;

import com.youpeng.jpowl.core.context.MonitorContext;
import com.youpeng.jpowl.core.context.MonitorContextHolder;
import com.youpeng.jpowl.core.handler.MonitorEventHandlerChain;
import com.youpeng.jpowl.core.model.MonitorData;
import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.metrics.MetricsCollector;
import com.youpeng.jpowl.core.sampler.MonitorSampler;
import com.youpeng.jpowl.core.filter.MonitorFilter;
import com.youpeng.jpowl.core.filter.DefaultMonitorFilter;
import com.youpeng.jpowl.core.util.CoreUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 监控管理器
 * 负责管理监控生命周期和协调各个组件
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MonitorManager {
    private static final Logger logger = LoggerFactory.getLogger(MonitorManager.class);
    
    private final OutputSource outputSource;                // 输出源
    private final ExecutorService executorService;          // 线程池
    private final ConcurrentHashMap<String, MonitorContext> activeContexts;  // 活动上下文
    private final MonitorEventHandlerChain handlerChain;    // 处理链
    private final MonitorSampler sampler;                   // 采样器
    private final MonitorFilter filter;                     // 过滤器
    private volatile boolean running = true;                // 运行状态

    /**
     * 构造监控管理器
     *
     * @param outputSource 输出源
     * @param config 监控配置
     */
    public MonitorManager(OutputSource outputSource, MonitorConfig config) {
        this.outputSource = outputSource;
        this.executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );
        this.activeContexts = new ConcurrentHashMap<>();
        this.handlerChain = new MonitorEventHandlerChain();
        this.sampler = new MonitorSampler(config.getSamplingRate());
        this.filter = new DefaultMonitorFilter();
    }

    /**
     * 开始监控
     *
     * @param name 监控名称
     * @return 监控上下文
     */
    public MonitorContext startMonitor(String name) {
        if (!running) {
            return null;
        }
        String traceId = CoreUtils.generateId();
        MonitorContext context = new MonitorContext(traceId);
        activeContexts.put(traceId, context);
        MonitorContextHolder.setContext(context);
        return context;
    }

    /**
     * 结束监控
     *
     * @param context 监控上下文
     */
    public void endMonitor(MonitorContext context) {
        try {
            if (context != null) {
                MonitorData data = context.getMonitorData();
                if (data != null && sampler.shouldSample() && filter.accept(data)) {
                    MetricsCollector.incrementCounter("monitor.processed");
                    handlerChain.handle(data);
                    outputSource.write(data);
                }
            }
        } catch (Exception e) {
            MetricsCollector.incrementCounter("monitor.errors");
            logger.error("Failed to process monitor data", e);
        } finally {
            if (context != null) {
                activeContexts.remove(context.getTraceId());
                MonitorContextHolder.clearContext();
            }
        }
    }

    /**
     * 关闭监控管理器
     */
    public void shutdown() {
        running = false;
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            outputSource.close();
        }
    }
} 