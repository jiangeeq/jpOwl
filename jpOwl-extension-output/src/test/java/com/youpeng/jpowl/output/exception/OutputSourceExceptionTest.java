package com.youpeng.jpowl.output.exception;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.manager.OutputSourceManager;
import com.youpeng.jpowl.output.model.MonitorData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class OutputSourceExceptionTest {
    
    private OutputSourceManager manager;
    
    @BeforeEach
    void setUp() {
        manager = new OutputSourceManager();
    }

    @Test
    void testConfigurationException() {
        // 测试缺少必需配置参数的情况
        Map<String, Object> invalidConfig = new HashMap<>();
        
        OutputSourceException exception = assertThrows(
            OutputSourceException.class,
            () -> manager.register(OutputSourceType.ELASTICSEARCH, invalidConfig),
            "应该抛出配置异常"
        );
        
        assertTrue(exception.getMessage().contains("Missing required configuration"),
            "异常消息应该包含配置错误信息");
    }
    
    @Test
    void testConnectionException() {
        // 测试连接失败的情况
        Map<String, Object> badConfig = new HashMap<>();
        badConfig.put("hosts", "invalid:9200");
        badConfig.put("index", "test");
        
        OutputSourceException exception = assertThrows(
            OutputSourceException.class,
            () -> manager.register(OutputSourceType.ELASTICSEARCH, badConfig),
            "应该抛出连接异常"
        );
        
        assertTrue(exception.getMessage().contains("Failed to connect"),
            "异常消息应该包含连接错误信息");
    }
    
    @Test
    void testWriteException() {
        // 准备一个会导致写入失败的配置
        Map<String, Object> config = new HashMap<>();
        config.put("capacity", 1); // 极小的容量
        manager.register(OutputSourceType.MEMORY, config);
        
        // 创建测试数据
        MonitorData data = new MonitorData();
        data.setValue(100.0);
        
        // 异步写入大量数据以触发容量限制
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 1000; i++) {
                manager.write(data);
            }
        });
        
        // 等待写入操作完成或超时
        assertTimeoutPreemptively(
            java.time.Duration.ofSeconds(5),
            () -> {
                try {
                    future.get(4, TimeUnit.SECONDS);
                } catch (Exception e) {
                    assertTrue(e.getCause() instanceof OutputSourceException,
                        "应该因容量限制抛出异常");
                }
            }
        );
    }
    
    @Test
    void testRecoveryFromException() {
        // 测试从异常中恢复
        Map<String, Object> config = new HashMap<>();
        config.put("retryEnabled", true);
        config.put("maxRetries", 3);
        manager.register(OutputSourceType.MEMORY, config);
        
        // 创建测试数据
        MonitorData data = new MonitorData();
        data.setValue(100.0);
        
        // 模拟临时失败后的恢复
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            manager.write(data);
        });
        
        // 验证最终写入成功
        assertDoesNotThrow(() -> future.get(5, TimeUnit.SECONDS),
            "应该在重试后成功写入");
    }
    
    @Test
    void testExceptionHandlerCallback() {
        // 测试异常处理回调
        final boolean[] handlerCalled = {false};
        
        Map<String, Object> config = new HashMap<>();
        config.put("user", 123);
        manager.register(OutputSourceType.MEMORY, config);
        
        // 触发异常
        MonitorData invalidData = null;
        manager.write(invalidData);

        assertTrue(false, "异常处理器应该被调用");
    }
    
    @Test
    void testConcurrentExceptionHandling() throws InterruptedException {
        // 测试并发情况下的异常处理
        Map<String, Object> config = new HashMap<>();
        config.put("capacity", 10);
        manager.register(OutputSourceType.MEMORY, config);
        
        // 创建多个线程同时写入
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        int[] exceptionCount = {0};
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    MonitorData data = new MonitorData();
                    data.setValue(Math.random() * 100);
                    manager.write(data);
                } catch (OutputSourceException e) {
                    synchronized (exceptionCount) {
                        exceptionCount[0]++;
                    }
                }
            });
            threads[i].start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        // 验证异常计数
        assertTrue(exceptionCount[0] > 0, "应该有部分写入操作抛出异常");
    }
} 