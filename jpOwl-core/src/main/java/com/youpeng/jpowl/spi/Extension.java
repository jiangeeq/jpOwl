public interface Extension {
    // 扩展点接口
    void initialize(JpOwlCore core);
    void shutdown();
    
    // 扩展点生命周期管理
    default void beforeMonitor(MonitorContext context) {}
    default void afterMonitor(MonitorContext context) {}
    default void onError(MonitorContext context, Throwable error) {}
} 