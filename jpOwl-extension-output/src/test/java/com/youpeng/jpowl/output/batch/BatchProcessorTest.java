package com.youpeng.jpowl.output.batch;

import com.youpeng.jpowl.output.model.MonitorData;
import com.youpeng.jpowl.output.util.ThreadPoolFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BatchProcessorTest {

    @Test
    void testBatchProcessing() throws InterruptedException {
        // 用于收集处理后的数据
        List<MonitorData> processedData = new ArrayList<>();
        // 用于等待批处理完成的闭锁
        CountDownLatch latch = new CountDownLatch(1);
        
        // 创建调度器
        ScheduledExecutorService scheduler = ThreadPoolFactory.createBatchProcessorPool("test-batch");
        
        // 创建批处理配置
        BatchProcessorConfig config = new BatchProcessorConfig();
        config.setBatchSize(5);
        config.setQueueCapacity(100);
        config.setFlushInterval(1000);
        config.setTimeUnit(TimeUnit.MILLISECONDS);
        config.setScheduler(scheduler);
        
        try {
            // 创建批处理器
            BatchProcessor processor = new BatchProcessor(config, batch -> {
                processedData.addAll(batch);
                if (processedData.size() >= 5) {
                    latch.countDown();
                }
            });
            
            // 添加测试数据
            for (int i = 0; i < 5; i++) {
                MonitorData data = new MonitorData();
                data.setValue(i);
                processor.add(data);
            }
            
            // 等待处理完成,设置超时时间为2秒
            assertTrue(latch.await(2, TimeUnit.SECONDS), "批处理未在预期时间内完成");
            assertEquals(5, processedData.size(), "处理的数据数量不符合预期");
            
            // 验证处理后的数据值
            for (int i = 0; i < 5; i++) {
                assertEquals(i, processedData.get(i).getValue(), "数据值不符合预期");
            }
        } finally {
            // 关闭调度器
            scheduler.shutdown();
        }
    }
    
    @Test
    void testBatchProcessorConfig() {
        // 测试配置验证
        BatchProcessorConfig config = new BatchProcessorConfig();
        config.setBatchSize(-1); // 设置无效的批处理大小
        
        assertThrows(IllegalArgumentException.class, () -> {
            config.validate();
        }, "应该检测到无效的批处理大小");
    }
} 