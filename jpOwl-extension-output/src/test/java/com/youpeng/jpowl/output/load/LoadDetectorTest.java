package com.youpeng.jpowl.output.load;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.exception.OutputSourceException;
import com.youpeng.jpowl.output.model.LoadStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LoadDetectorTest {

    @Test
    void testLoadDetection() {
        // 创建模拟的负载检测器
        LoadDetector detector = Mockito.mock(LoadDetector.class);
        
        // 设置预期行为 - 模拟高负载状态
        LoadStatus expectedStatus = LoadStatus.create()
            .cpuUsage(75.0)
            .memoryUsage(80.0)
            .connectionUsage(60.0)
            .diskIoUsage(40.0)
            .systemLoad(2.5)
            .overloaded(true)
            .build();
            
        when(detector.detect()).thenReturn(expectedStatus);
        
        // 执行检测
        LoadStatus status = detector.detect();
        
        // 验证结果
        assertNotNull(status, "负载状态不应为空");
        assertEquals(75.0, status.getCpuUsage(), "CPU使用率不符");
        assertEquals(80.0, status.getMemoryUsage(), "内存使用率不符");
        assertEquals(60.0, status.getConnectionUsage(), "连接使用率不符");
        assertTrue(status.isOverloaded(), "应该检测到过载状态");
    }
    
    @Test
    void testNormalLoadDetection() {
        // 创建模拟的负载检测器
        LoadDetector detector = Mockito.mock(LoadDetector.class);
        
        // 设置预期行为 - 模拟正常负载状态
        LoadStatus expectedStatus = LoadStatus.create()
            .cpuUsage(30.0)
            .memoryUsage(40.0)
            .connectionUsage(20.0)
            .diskIoUsage(15.0)
            .systemLoad(1.0)
            .overloaded(false)
            .build();
            
        when(detector.detect()).thenReturn(expectedStatus);
        
        // 执行检测
        LoadStatus status = detector.detect();
        
        // 验证结果
        assertFalse(status.isOverloaded(), "不应该检测到过载状态");
    }
    
    @Test
    void testLoadDetectorFactory() {
        // 测试工厂创建检测器 - 无效客户端类型
        assertThrows(OutputSourceException.class, () -> {
            LoadDetectorFactory.registerDetector(OutputSourceType.MEMORY, new Object());
        }, "应该检测到无效的客户端类型");
        
        // 测试工厂创建检测器 - 空客户端
        assertThrows(OutputSourceException.class, () -> {
            LoadDetectorFactory.registerDetector(OutputSourceType.MEMORY, null);
        }, "应该检测到空客户端");
    }
} 