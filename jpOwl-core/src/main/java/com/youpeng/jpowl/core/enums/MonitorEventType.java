package com.youpeng.jpowl.core.enums;

/**
 * 监控事件类型枚举
 * 定义了系统支持的所有监控事件类型
 * 
 * @author youpeng
 * @since 1.0.0
 */
public enum MonitorEventType {
    /**
     * 事务类型，用于记录业务操作
     */
    TRANSACTION,
    
    /**
     * 指标类型，用于记录性能指标
     */
    METRIC,
    
    /**
     * 事件类型，用于记录系统事件
     */
    EVENT,
    
    /**
     * 心跳类型，用于健康检查
     */
    HEARTBEAT,
    
    /**
     * 告警类型，用于系统告警
     */
    ALERT;
} 