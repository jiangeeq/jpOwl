package com.youpeng.jpowl.core.context;

/**
 * 监控上下文持有者
 */
public final class MonitorContextHolder {
    private static final ThreadLocal<MonitorContext> contextHolder = new ThreadLocal<>();
    
    private MonitorContextHolder() {}
    
    public static void setContext(MonitorContext context) {
        contextHolder.set(context);
    }
    
    public static MonitorContext getContext() {
        return contextHolder.get();
    }
    
    public static void clearContext() {
        contextHolder.remove();
    }
} 