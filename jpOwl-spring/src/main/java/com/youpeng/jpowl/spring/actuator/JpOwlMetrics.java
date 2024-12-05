@Component
public class JpOwlMetrics {
    
    private final JpOwlCore jpOwlCore;
    
    public JpOwlMetrics(JpOwlCore jpOwlCore, MeterRegistry registry) {
        this.jpOwlCore = jpOwlCore;
        
        // 注册核心指标
        Gauge.builder("jpowl.monitor.count", jpOwlCore, this::getMonitorCount)
             .description("Total number of monitored operations")
             .register(registry);
             
        Gauge.builder("jpowl.output.queue.size", jpOwlCore, this::getQueueSize)
             .description("Current size of output queue")
             .register(registry);
    }
    
    private long getMonitorCount() {
        return jpOwlCore.getMetrics().getOrDefault("monitorCount", 0L);
    }
    
    private int getQueueSize() {
        return jpOwlCore.getMetrics().getOrDefault("queueSize", 0);
    }
} 