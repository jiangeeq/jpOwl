package com.youpeng.jpowl.output.event;

import com.youpeng.jpowl.output.core.OutputSourceType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class OutputSourceEventListenerTest {

    @Test
    void testEventHandling() throws InterruptedException {
        // 创建模拟的事件处理器
        OutputSourceEventHandler mockHandler = Mockito.mock(OutputSourceEventHandler.class);
        
        // 注册事件处理器
        OutputSourceEventListener.registerHandler(mockHandler);
        
        try {
            // 创建事件
            OutputSourceEvent event = OutputSourceEvent.create()
                .type(OutputSourceEvent.EventType.WRITE_SUCCESS)
                .sourceType(OutputSourceType.MEMORY)
                .details("Test event")
                .build();
                
            // 发布事件
            OutputSourceEventListener.publishEvent(event);
            
            // 验证事件处理
            verify(mockHandler).handleEvent(event);
        } finally {
            // 清理
            OutputSourceEventListener.removeHandler(mockHandler);
        }
    }
    
    @Test
    void testMultipleHandlers() {
        CountDownLatch latch = new CountDownLatch(2);
        
        // 创建两个处理器
        OutputSourceEventHandler handler1 = event -> latch.countDown();
        OutputSourceEventHandler handler2 = event -> latch.countDown();
        
        // 注册处理器
        OutputSourceEventListener.registerHandler(handler1);
        OutputSourceEventListener.registerHandler(handler2);
        
        try {
            // 发布事件
            OutputSourceEvent event = OutputSourceEventListener.eventBuilder()
                .type(OutputSourceEvent.EventType.WRITE_SUCCESS)
                .sourceType(OutputSourceType.MEMORY)
                .build();
                
            OutputSourceEventListener.publishEvent(event);
            
            // 验证所有处理器都被调用
            assertTrue(latch.await(1, TimeUnit.SECONDS), "所有处理器都应该被调用");
        } catch (InterruptedException e) {
            fail("测试被中断");
        } finally {
            // 清理
            OutputSourceEventListener.removeHandler(handler1);
            OutputSourceEventListener.removeHandler(handler2);
        }
    }
} 