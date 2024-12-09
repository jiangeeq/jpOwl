package com.youpeng.jpowl.output.metrics;

import com.youpeng.jpowl.output.core.OutputSourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetricsCollectorTest {

    @Test
    void testMetricsCollection() {
        OutputSourceType type = OutputSourceType.MEMORY;
        
        // 记录成功写入
        MetricsCollector.recordWrite(type, true, 100);
        MetricsCollector.recordWrite(type, true, 150);
        
        // 记录失败写入
        MetricsCollector.recordWrite(type, false, 0);
        
        // 获取指标并验证
        OutputSourceMetrics metrics = MetricsCollector.getMetrics(type);
        
        assertEquals(3, metrics.getTotalWrites().get());
        assertEquals(2, metrics.getSuccessWrites().get());
        assertEquals(1, metrics.getFailedWrites().get());
        assertEquals(125.0, metrics.getAverageLatency(), 0.01);
    }
} 