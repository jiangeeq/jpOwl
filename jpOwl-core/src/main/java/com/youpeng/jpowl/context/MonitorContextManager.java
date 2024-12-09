package com.youpeng.jpowl.context;

import com.youpeng.jpowl.model.MonitorModel;
import com.youpeng.jpowl.collector.DataCollector;

public class MonitorContextManager {
    private final DataCollector collector;
    private static final ThreadLocal<MonitorContext> contextHolder = new ThreadLocal<>();

    public MonitorContextManager(DataCollector collector) {
        this.collector = collector;
    }

    public void beginContext() {
        contextHolder.set(new MonitorContext());
    }

    public void endContext() {
        MonitorContext context = contextHolder.get();
        if (context != null) {
            // 收集上下文中的监控数据
            context.getModels().forEach(collector::collect);
            contextHolder.remove();
        }
    }

    public void addModel(MonitorModel model) {
        MonitorContext context = contextHolder.get();
        if (context != null) {
            context.addModel(model);
        } else {
            // 如果没有上下文，直接收集
            collector.collect(model);
        }
    }
} 