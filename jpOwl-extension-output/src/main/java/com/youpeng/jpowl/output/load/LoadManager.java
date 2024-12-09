package com.youpeng.jpowl.output.load;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.event.OutputSourceEvent;
import com.youpeng.jpowl.output.event.OutputSourceEventListener;
import com.youpeng.jpowl.output.model.LoadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.youpeng.jpowl.output.util.ThreadPoolFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 负载管理器
 */
public class LoadManager {
    private static final Logger logger = LoggerFactory.getLogger(LoadManager.class);

    private static final Map<OutputSourceType, LoadContext> contextMap = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = ThreadPoolFactory.createScheduledPool("load-manager");
    private static final long DEFAULT_CHECK_INTERVAL = 5000; // 默认5秒检查一次

    /**
     * 启动负载监控
     */
    public static void startMonitoring(OutputSourceType type, long checkIntervalMs) {
        LoadDetector detector = LoadDetectorFactory.getDetector(type);
        LoadContext context = new LoadContext(type, detector,null);

        // 启动定期检查任务
        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(
            () -> checkLoad(context),
            0,
            checkIntervalMs,
            TimeUnit.MILLISECONDS
        );
        context.setMonitoringTask(future);
        contextMap.put(type, context);

        logger.info("Started load monitoring for {} with interval {}ms", type, checkIntervalMs);
    }
    /**
     * 注册负载检测器
     */
    public static void register(OutputSourceType type, LoadDetector detector) {
        try {
            LoadContext context = new LoadContext(type, detector, null);
            // 创建定时检测任务
            ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(
                    () -> checkLoad(context),
                    DEFAULT_CHECK_INTERVAL,
                    DEFAULT_CHECK_INTERVAL,
                    TimeUnit.MILLISECONDS
            );
            context.setMonitoringTask(future);
            contextMap.put(type, context);

            logger.info("Registered load detector for {}", type);
        } catch (Exception e) {
            logger.error("Failed to register load detector for {}", type, e);
            throw new RuntimeException("Failed to register load detector", e);
        }
    }
    
    /**
     * 停止负载监控
     */
    public static void stopMonitoring(OutputSourceType type) throws InterruptedException {
        LoadContext context = contextMap.remove(type);
        if (context != null && context.getMonitoringTask() != null) {
            context.getMonitoringTask().cancel(false);
            logger.info("Stopped load monitoring for {}", type);
        }
        // 关闭调度器
        scheduler.shutdown();
        if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
            scheduler.shutdownNow();
        }
    }
    
    private static void checkLoad(LoadContext context) {
        try {
            LoadStatus status = context.getDetector().detect();
            
            // 发布负载状态事件
            OutputSourceEventListener.publishEvent(
                OutputSourceEventListener.eventBuilder()
                    .type(OutputSourceEvent.EventType.LOAD_STATUS)
                    .sourceType(context.getType())
                    .details(String.format("Load status - CPU: %.1f%%, Memory: %.1f%%, IO: %.1f%%",
                        status.getCpuUsage(), status.getMemoryUsage(), status.getDiskIoUsage()))
                    .build()
            );
            
            // 如果检测到过载，发布过载事件
            if (status.isOverloaded()) {
                OutputSourceEventListener.publishEvent(
                    OutputSourceEventListener.eventBuilder()
                        .type(OutputSourceEvent.EventType.OVERLOADED)
                        .sourceType(context.getType())
                        .details("System overloaded")
                        .build()
                );
            }
            
        } catch (Exception e) {
            logger.error("Failed to check load for {}", context.getType(), e);
        }
    }
    

    private static class LoadContext {
        private final OutputSourceType type;
        private final LoadDetector detector;
        private ScheduledFuture<?> monitoringTask;

        public LoadContext(OutputSourceType type, LoadDetector detector, ScheduledFuture<?> monitoringTask) {
            this.type = type;
            this.detector = detector;
            this.monitoringTask = monitoringTask;
        }

        public OutputSourceType getType() {
            return type;
        }

        public LoadDetector getDetector() {
            return detector;
        }

        public ScheduledFuture<?> getMonitoringTask() {
            return monitoringTask;
        }

        public void setMonitoringTask(ScheduledFuture<?> monitoringTask) {
            this.monitoringTask = monitoringTask;
        }
    }
} 