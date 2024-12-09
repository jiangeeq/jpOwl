package com.youpeng.jpowl.spring.actuator;

import com.youpeng.jpowl.core.JpOwlCore;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot Actuator 集成
 * 提供监控指标暴露功能
 */
@Endpoint(id = "jpowl")
public class JpOwlMetrics {
    private final JpOwlCore jpOwlCore;
    private final MeterRegistry meterRegistry;

    public JpOwlMetrics(JpOwlCore jpOwlCore, MeterRegistry meterRegistry) {
        this.jpOwlCore = jpOwlCore;
        this.meterRegistry = meterRegistry;
        initMetrics();
    }

    private void initMetrics() {
        // 注册方法执行时间指标
        meterRegistry.gauge("jpowl.method.duration", Tags.empty(), jpOwlCore, 
            core -> core.getAverageExecutionTime());
            
        // 注册错误率指标
        meterRegistry.gauge("jpowl.method.error.rate", Tags.empty(), jpOwlCore,
            core -> core.getErrorRate());
    }

    @ReadOperation
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalExecutions", jpOwlCore.getTotalExecutions());
        metrics.put("averageExecutionTime", jpOwlCore.getAverageExecutionTime());
        metrics.put("errorCount", jpOwlCore.getErrorCount());
        metrics.put("errorRate", jpOwlCore.getErrorRate());
        return metrics;
    }
} 