package com.youpeng.jpowl.logging;

import com.youpeng.jpowl.logging.adapter.LoggerAdapter;
import com.youpeng.jpowl.logging.decorator.ExceptionDecorator;
import com.youpeng.jpowl.logging.decorator.TraceIdDecorator;
import com.youpeng.jpowl.logging.filter.LevelFilter;
import com.youpeng.jpowl.logging.handler.MDCHandler;
import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogEventBuilder;
import com.youpeng.jpowl.logging.model.LogLevel;
import com.youpeng.jpowl.logging.monitor.LoggingMetrics;
import com.youpeng.jpowl.logging.monitor.MetricsSnapshot;
import com.youpeng.jpowl.logging.process.AsyncLogProcessor;
import com.youpeng.jpowl.logging.process.BatchLogProcessor;
import com.youpeng.jpowl.logging.process.LogProcessingChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerFactoryTest {
    private LogProcessingChain processingChain;
    private LoggingMetrics metrics;
    private AsyncLogProcessor asyncProcessor;
    private BatchLogProcessor batchProcessor;

    
    @BeforeEach
    void setUp() {
        // 初始化指标收集器
        metrics = new LoggingMetrics();
        
        // 初始化处理链
        processingChain = new LogProcessingChain(metrics);
        
        // 添加过滤器
        processingChain.addFilter(new LevelFilter(LogLevel.INFO));
        
        // 添加装饰器
        processingChain.addDecorator(new ExceptionDecorator());
        processingChain.addDecorator(new TraceIdDecorator("traceId", () -> UUID.randomUUID().toString()));
        
        // 初始化指标收集器
        metrics = new LoggingMetrics();
        processingChain.addHandler(new MDCHandler());
        
        // 初始化异步处理器
        asyncProcessor = new AsyncLogProcessor(processingChain);
        
        // 初始化批处理器
        batchProcessor = new BatchLogProcessor(processingChain, 100, 1000);
    }
    
    @Test
    void testCompleteLoggingFlow() throws Exception {
        // 1. 创建日志事件
        LogEvent event = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.INFO)
                .message("Test message with parameter: {}")
                .args(new Object[]{"test value"})
                .context("requestId", UUID.randomUUID().toString())
                .build();
        
        // 2. 测试同步处理
        processingChain.process(event);
        
        // 3. 测试异步处理
        asyncProcessor.process(event);
        
        // 4. 测试批处理
        batchProcessor.process(event);
        
        // 等待异步处理完成
        TimeUnit.SECONDS.sleep(2);
        
        // 5. 验证指标
        assertNotNull(metrics.getSnapshot());
        assertTrue(metrics.getSnapshot().getTotalLogs() > 0);
        
        // 6. 测试日志级别设置
        LoggerFactory.setLevel("com.example.test", LogLevel.DEBUG);
        assertEquals(LogLevel.DEBUG, LoggerFactory.getLevel("com.example.test"));
        
        // 7. 测试异常场景
        LogEvent errorEvent = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.ERROR)
                .message("Test error")
                .throwable(new RuntimeException("Test exception"))
                .build();
        
        processingChain.process(errorEvent);
        
        // 8. 清理资源
        asyncProcessor.shutdown();
        batchProcessor.shutdown();
        LoggerFactory.shutdown();
    }
    
    @Test
    void testLoggerAdapterInitialization() {
        // 测试日志适配器是否正确初始化
        assertNotNull(LoggerFactory.getLogger("test"));
    }
    
    @Test
    void testMetricsCollection() throws Exception {
        // 生成一些测试日志
        for (int i = 0; i < 10; i++) {
            LogEvent event = LogEventBuilder.create()
                    .loggerName("test.metrics")
                    .level(i % 2 == 0 ? LogLevel.INFO : LogLevel.ERROR)
                    .message("Test message " + i)
                    .build();
            
            processingChain.process(event);
        }
        LoggingMetrics metrics1 = processingChain.getMetrics();
        // 等待处理完成
        TimeUnit.SECONDS.sleep(1);

        // 验证指标
        // todo 猜测是测试框架问题，无法获取到指标 metrics 变成了新的new对象，看代码引用的是同一个对象，所以采用2次赋值
        metrics = metrics1;
        MetricsSnapshot snapshot = metrics.getSnapshot();
        assertTrue(snapshot.getTotalLogs() >= 9);
        assertTrue(snapshot.getErrorCount() >= 3);
    }
    
    @Test
    void testLogPrinting() {
        // 获取日志适配器
        LoggerAdapter logger = LoggerFactory.getLogger("com.example.test");
        
        // 测试不同级别的日志打印
        LogEvent infoEvent = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.INFO)
                .message("This is an info message")
                .build();
        logger.log(infoEvent);
        
        LogEvent debugEvent = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.DEBUG)
                .message("This is a debug message")
                .build();
        logger.log(debugEvent);
        
        // 测试带参数的日志
        LogEvent paramEvent = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.INFO)
                .message("Message with parameters: {} and {}")
                .args(new Object[]{"param1", "param2"})
                .build();
        logger.log(paramEvent);
        
        // 测试带异常的日志
        Exception testException = new RuntimeException("Test Exception");
        LogEvent errorEvent = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.ERROR)
                .message("Error occurred")
                .throwable(testException)
                .build();
        logger.log(errorEvent);
        
        // 测试带MDC的日志
        LogEvent mdcEvent = LogEventBuilder.create()
                .loggerName("com.example.test")
                .level(LogLevel.INFO)
                .message("Message with MDC")
                .context("requestId", UUID.randomUUID().toString())
                .context("userId", "12345")
                .build();
        logger.log(mdcEvent);
    }
} 