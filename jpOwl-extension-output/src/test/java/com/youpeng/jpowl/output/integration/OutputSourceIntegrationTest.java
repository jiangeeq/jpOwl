package com.youpeng.jpowl.output.integration;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.manager.OutputSourceManager;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import com.youpeng.jpowl.output.model.MonitorData;
import com.youpeng.jpowl.output.model.OutputSourceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class OutputSourceIntegrationTest {
    
    private OutputSourceManager manager;
    
    @BeforeEach
    void setUp() {
        manager = new OutputSourceManager();
    }

    @Test
    void testMemoryOutputSourceIntegration() throws InterruptedException {
        // 准备配置
        Map<String, Object> properties = new HashMap<>();
        properties.put("capacity", 1000);
        properties.put("batchSize", 100);
        
        // 注册输出源
        manager.register(OutputSourceType.MEMORY, properties);
        
        // 创建批量测试数据
        List<MonitorData> dataList = createTestDataBatch(500);
        
        // 用于等待写入完成
        CountDownLatch latch = new CountDownLatch(1);
        
        // 批量写入数据
        manager.writeBatch(dataList);
        
        // 等待写入完成
        assertTrue(latch.await(5, TimeUnit.SECONDS), "批量写入操作超时");
        
        // 验证写入结果
        assertEquals(500, MetricsCollector.getMetrics(OutputSourceType.MEMORY).getTotalWrites().get(),
            "写入总数不符");
    }
    
    @Test
    void testFileOutputSourceIntegration() throws Exception {
        // 准备文件输出源配置
        Map<String, Object> properties = new HashMap<>();
        properties.put("filePath", "test-output.log");
        properties.put("bufferSize", 8192);
        
        // 注册输出源
        manager.register(OutputSourceType.FILE, properties);
        
        // 写入测试数据
        MonitorData data = createTestData();
        manager.write(data);
        
        // 验证文件写入
        // TODO: 添加文件内容验证
    }

    private MonitorData createTestData() {
        MonitorData data = new MonitorData();
        data.setTimestamp(Instant.now());
        data.setMetricName("integration_test_metric");
        data.setValue(123.45);
        
        Map<String, String> tags = new HashMap<>();
        tags.put("host", "test-host");
        tags.put("env", "test");
        data.setTags(tags);
        
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("region", "test-region");
        attrs.put("version", "1.0.0");
        data.setAttributes(attrs);
        
        return data;
    }
    
    private List<MonitorData> createTestDataBatch(int size) {
        List<MonitorData> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MonitorData data = createTestData();
            data.setValue(i);
            dataList.add(data);
        }
        return dataList;
    }
} 