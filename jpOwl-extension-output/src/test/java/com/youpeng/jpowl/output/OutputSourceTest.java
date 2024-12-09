package com.youpeng.jpowl.output;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.manager.OutputSourceManager;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import com.youpeng.jpowl.output.metrics.OutputSourceMetrics;
import com.youpeng.jpowl.output.model.MonitorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class OutputSourceTest {
    private OutputSourceManager manager;
    
    @BeforeEach
    void setUp() {
        manager = new OutputSourceManager();
    }
    
    @Test
    void testMemoryOutputSource() throws InterruptedException {
        // 准备配置
        Map<String, Object> properties = new HashMap<>();
        properties.put("capacity", 1000);
        
        // 注册输出源
        manager.register(OutputSourceType.MEMORY, properties);
        
        // 准备测试数据
        MonitorData data = createTestData();
        
        // 用于等待异步写入完成
        CountDownLatch latch = new CountDownLatch(1);
        
        // 写入数据
        manager.write(data);
        
        // 等待写入完成
        assertTrue(latch.await(1, TimeUnit.SECONDS), "写入操作超时");
        
        // 验证写入结果
        OutputSourceMetrics metrics = MetricsCollector.getMetrics(OutputSourceType.MEMORY);
        assertEquals(1, metrics.getTotalWrites().get(), "总写入次数不符");
        assertEquals(1, metrics.getSuccessWrites().get(), "成功写入次数不符");
    }
    
    @Test
    void testBatchWrite() throws InterruptedException {
        // 准备配置
        Map<String, Object> properties = new HashMap<>();
        properties.put("batchSize", 100);
        manager.register(OutputSourceType.MEMORY, properties);
        
        // 创建测试数据
        List<MonitorData> dataList = createTestDataBatch(10);
        
        // 用于等待批量写入完成
        CountDownLatch latch = new CountDownLatch(1);
        
        // 执行批量写入
        manager.writeBatch(dataList);
        
        // 等待写入完成
        assertTrue(latch.await(1, TimeUnit.SECONDS), "批量写入操作超时");
        
        // 验证写入结果
        OutputSourceMetrics metrics = MetricsCollector.getMetrics(OutputSourceType.MEMORY);
        assertEquals(10, metrics.getTotalWrites().get(), "批量写入总数不符");
    }
    
    @Test
    void testInvalidConfiguration() {
        // 测试无效配置
        Map<String, Object> properties = new HashMap<>();
        properties.put("capacity", -1); // 无效的容量值
        
        assertThrows(IllegalArgumentException.class, () -> {
            manager.register(OutputSourceType.MEMORY, properties);
        }, "应该检测到无效的配置");
    }
    
    private MonitorData createTestData() {
        MonitorData data = new MonitorData();
        data.setTimestamp(Instant.now());
        data.setMetricName("test_metric");
        data.setValue(123.45);
        
        Map<String, String> tags = new HashMap<>();
        tags.put("host", "test-host");
        data.setTags(tags);
        
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("region", "test-region");
        data.setAttributes(attrs);
        
        return data;
    }
    
    private List<MonitorData> createTestDataBatch(int size) {
        List<MonitorData> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MonitorData data = createTestData();
            data.setValue(i); // 设置不同的值以区分
            dataList.add(data);
        }
        return dataList;
    }
} 