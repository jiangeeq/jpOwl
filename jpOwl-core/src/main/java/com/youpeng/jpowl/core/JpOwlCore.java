public class JpOwlCore {
    private final MonitorManager monitorManager;
    private final MetricAggregator metricAggregator;
    private final OutputHandler outputHandler;
    
    public JpOwlCore(JpOwlCoreConfig config) {
        this.outputHandler = createOutputHandler(config);
        this.monitorManager = new MonitorManager(outputHandler);
        this.metricAggregator = new MetricAggregator();
    }
    
    private OutputHandler createOutputHandler(JpOwlCoreConfig config) {
        return switch (config.getOutputType()) {
            case FILE -> new AsyncFileOutput(config.getFilePath());
            case MEMORY -> new MemoryOutput();
            case ELASTICSEARCH -> new ElasticsearchOutput();
            case MONGODB -> new MongoOutput();
            default -> new AsyncFileOutput(config.getFilePath());
        };
    }
    
    // 核心API方法
    public void logTransaction(String name, long duration) {
        monitorManager.logTransaction(new Transaction(name, duration));
    }
    
    public void logEvent(String name, String detail) {
        monitorManager.logEvent(new Event(name, detail));
    }
    
    public void shutdown() {
        monitorManager.shutdown();
        metricAggregator.shutdown();
    }
} 