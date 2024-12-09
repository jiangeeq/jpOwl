package com.youpeng.jpowl.output.manager;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.event.OutputSourceEvent;
import com.youpeng.jpowl.output.event.OutputSourceEventListener;
import com.youpeng.jpowl.output.model.OutputSourceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 输出源状态管理器
 */
public class OutputSourceStatusManager {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceStatusManager.class);

    private static final Map<OutputSourceType, OutputSourceStatus> statusMap = new ConcurrentHashMap<>();
    
    /**
     * 更新状态
     */
    public static void updateStatus(OutputSourceType type, OutputSourceStatus.Status status) {
        OutputSourceStatus currentStatus = statusMap.computeIfAbsent(type, 
            t -> OutputSourceStatus.create()
                .type(t)
                .status(OutputSourceStatus.Status.CREATED)
                .createdTime(Instant.now())
                .build());
        
        currentStatus.status(status);
        currentStatus.lastUpdatedTime(Instant.now());
        
        // 发布状态变更
        OutputSourceEventListener.publishEvent(
            OutputSourceEventListener.eventBuilder()
                .type(convertStatusToEventType(status))
                .sourceType(type)
                .details("Status changed to " + status)
                .build()
        );
    }
    
    /**
     * 获取状态
     */
    public static OutputSourceStatus getStatus(OutputSourceType type) {
        return statusMap.get(type);
    }
    
    private static OutputSourceEvent.EventType convertStatusToEventType(OutputSourceStatus.Status status) {
        switch (status) {
            case RUNNING:
                return OutputSourceEvent.EventType.STARTED;
            case STOPPED:
                return OutputSourceEvent.EventType.STOPPED;
            case ERROR:
                return OutputSourceEvent.EventType.ERROR;
            default:
                return null;
        }
    }
} 