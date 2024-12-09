package com.youpeng.jpowl.output.event;

import com.youpeng.jpowl.output.metrics.MetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 输出源事件监听器
 */
public class OutputSourceEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceEventListener.class);

    private static final List<OutputSourceEventHandler> handlers = new CopyOnWriteArrayList<>();
    
    /**
     * 注册事件处理器
     */
    public static void registerHandler(OutputSourceEventHandler handler) {
        handlers.add(handler);
    }
    
    /**
     * 移除事件处理器
     */
    public static void removeHandler(OutputSourceEventHandler handler) {
        handlers.remove(handler);
    }
    
    /**
     * 发布事件
     */
    public static void publishEvent(OutputSourceEvent event) {
        for (OutputSourceEventHandler handler : handlers) {
            try {
                handler.handleEvent(event);
            } catch (Exception e) {
                logger.error("Error handling event: {}", event, e);
            }
        }
        
        // 更新指标
        if (event.getType() == OutputSourceEvent.EventType.WRITE_SUCCESS 
            || event.getType() == OutputSourceEvent.EventType.WRITE_FAILED) {
            MetricsCollector.recordWrite(
                event.getSourceType(),
                event.getType() == OutputSourceEvent.EventType.WRITE_SUCCESS,
                0
            );
        }
    }
    
    /**
     * 创建事件构建器
     */
    public static OutputSourceEvent eventBuilder() {
        return OutputSourceEvent.create()
            .timestamp(Instant.now());
    }
} 