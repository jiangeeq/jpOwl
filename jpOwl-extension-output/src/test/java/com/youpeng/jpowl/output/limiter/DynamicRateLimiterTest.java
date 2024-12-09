package com.youpeng.jpowl.output.limiter;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.model.LoadStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicRateLimiterTest {
    private OutputSourceType testType = OutputSourceType.MEMORY;
    
    @BeforeEach
    void setUp() {
        // 创建限流器配置
        DynamicRateLimiter.LimiterConfig config = new DynamicRateLimiter.LimiterConfig();
        config.setInitialQps(1000);
        config.setMaxQps(2000);
        config.setMinQps(100);
        config.setAdjustmentStep(100);
        config.setSuccessRateThreshold(0.95);
        config.setLatencyThreshold(100);
        
        // 配置限流器
        DynamicRateLimiter.configure(testType, config);
    }
    
    @Test
    void testRateAdjustment() {
        // 获取当前上下文
        DynamicRateLimiter.LimiterContext context = DynamicRateLimiter.getContext(testType);
        assertNotNull(context, "限流器上下文不应为空");
        
        // 记录初始QPS
        int initialQps = context.getCurrentQps().get();
        
        // 模拟系统负载状态
        LoadStatus highLoad = LoadStatus.create()
            .cpuUsage(85.0)
            .memoryUsage(90.0)
            .diskIoUsage(70.0)
            .overloaded(true)
            .build();
            
        // 触发限流调整
        DynamicRateLimiter.adjustRate(context);
        
        // 验证QPS被降低
        assertTrue(context.getCurrentQps().get() < initialQps, "高负载时应该降低QPS");
        assertTrue(context.getCurrentQps().get() >= context.getConfig().getMinQps(), 
            "QPS不应低于最小值");
        
        // 模拟正常负载状态
        LoadStatus normalLoad = LoadStatus.create()
            .cpuUsage(30.0)
            .memoryUsage(40.0)
            .diskIoUsage(20.0)
            .overloaded(false)
            .build();
            
        // 重置QPS到初始值
        context.getCurrentQps().set(initialQps);
        
        // 触发限流调整
        DynamicRateLimiter.adjustRate(context);
        
        // 验证QPS被提高
        assertTrue(context.getCurrentQps().get() > initialQps, "低负载时应该提高QPS");
        assertTrue(context.getCurrentQps().get() <= context.getConfig().getMaxQps(), 
            "QPS不应超过最大值");
    }
    
    @Test
    void testAcquirePermits() throws InterruptedException {
        // 设置较低的QPS限制以便测试
        DynamicRateLimiter.LimiterContext context = DynamicRateLimiter.getContext(testType);
        context.getCurrentQps().set(2); // 每秒2个请求
        
        // 第一次获取许可应该成功
        assertTrue(DynamicRateLimiter.tryAcquire(testType), "首次获取许可应该成功");
        
        // 快速连续获取许可应该失败
        assertFalse(DynamicRateLimiter.tryAcquire(testType), "间隔太短时应该获取失败");
        
        // 等待计数器重置
        Thread.sleep(1100); // 等待超过1秒，让计数器重置
        
        // 重置后应该能再次获取许可
        assertTrue(DynamicRateLimiter.tryAcquire(testType), "计数器重置后应该能获取许可");
    }
    
    @Test
    void testConfigValidation() {
        DynamicRateLimiter.LimiterConfig invalidConfig = new DynamicRateLimiter.LimiterConfig();
        invalidConfig.setInitialQps(-1); // 设置无效的初始QPS
        
        assertThrows(IllegalArgumentException.class, () -> {
            DynamicRateLimiter.configure(testType, invalidConfig);
        }, "应该检测到无效的QPS配置");
    }
}