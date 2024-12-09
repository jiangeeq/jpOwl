package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.MonitorEventType;

/**
 * 事务监控事件
 * 用于记录业务事务的执行情况
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class TransactionEvent extends MonitorData {
    private final String transactionName;
    private final long duration;
    private final boolean success;

    /**
     * 构造事务事件
     *
     * @param id 事件ID
     * @param source 事件来源
     * @param transactionName 事务名称
     * @param duration 执行时长(毫秒)
     * @param success 是否成功
     */
    public TransactionEvent(String id, String source, String transactionName, long duration, boolean success) {
        super(id, MonitorEventType.TRANSACTION, source);
        this.transactionName = transactionName;
        this.duration = duration;
        this.success = success;
        
        // 添加事务相关指标
        addMetric("duration", duration);
        addMetric("success", success ? 1 : 0);
        addTag("transaction", transactionName);
    }

    public String getTransactionName() { return transactionName; }
    public long getDuration() { return duration; }
    public boolean isSuccess() { return success; }
} 