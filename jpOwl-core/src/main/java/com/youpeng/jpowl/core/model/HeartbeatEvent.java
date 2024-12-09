package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;
import java.util.Map;

/**
 * 心跳监控事件
 * 用于系统健康检查
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class HeartbeatEvent extends MonitorData {
    private final String serviceName;
    private final String status;
    private final Map<String, Object> healthData;

    /**
     * 构造心跳事件
     *
     * @param id 事件ID
     * @param source 事件来源
     * @param serviceName 服务名称
     * @param status 状态
     * @param healthData 健康数据
     */
    public HeartbeatEvent(String id, String source, String serviceName, 
                         String status, Map<String, Object> healthData) {
        super(id, MonitorEventType.HEARTBEAT, source);
        this.serviceName = serviceName;
        this.status = status;
        this.healthData = healthData;
        
        // 添加心跳相关数据
        addTag("service", serviceName);
        addTag("status", status);
        healthData.forEach(this::addMetric);
    }

    public String getServiceName() { return serviceName; }
    public String getStatus() { return status; }
    public Map<String, Object> getHealthData() { return healthData; }
} 