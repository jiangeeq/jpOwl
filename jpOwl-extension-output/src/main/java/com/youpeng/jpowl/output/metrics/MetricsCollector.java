package com.youpeng.jpowl.output.metrics;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.event.OutputSourceEventListener;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 指标收集器
 */
public class MetricsCollector {
    private static final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);

    private static MeterRegistry registry;
    private static final Map<OutputSourceType, OutputSourceMetrics> metricsMap = new ConcurrentHashMap<>();
    
    public static void init(MeterRegistry meterRegistry) {
        registry = meterRegistry;
    }
    
    public static void recordWrite(OutputSourceType type, boolean success, long duration) {
        if (registry == null) return;
        
        try {
            // 记录写入次数
            Counter.builder("jpowl.output.write.count")
                .tag("type", type.getCode())
                .tag("status", success ? "success" : "failure")
                .register(registry)
                .increment();
                
            // 记录写入延迟
            Timer.builder("jpowl.output.write.latency")
                .tag("type", type.getCode())
                .register(registry)
                .record(duration, TimeUnit.MILLISECONDS);
                
            // 更新指标统计
            metricsMap.computeIfAbsent(type, k -> new OutputSourceMetrics(OutputSourceType.ELASTICSEARCH))
                .recordWrite(success, duration);
                
        } catch (Exception e) {
            logger.error("Failed to record metrics", e);
        }
    }

    /**
     * 获取输出源的指标统计
     */
    public static OutputSourceMetrics getMetrics(OutputSourceType type) {
        return metricsMap.computeIfAbsent(type, k -> new OutputSourceMetrics(type));
    }

    public static MeterRegistry getRegistry() {
        return registry;
    }
}