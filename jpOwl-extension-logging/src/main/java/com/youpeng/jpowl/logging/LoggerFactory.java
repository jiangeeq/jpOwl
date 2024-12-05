public class LoggerFactory {
    private static final LoggerAdapter ADAPTER;
    
    static {
        // 按优先级尝试加载日志适配器
        LoggerAdapter adapter = null;
        try {
            // 首先尝试Logback
            adapter = new LogbackAdapter();
            if (!adapter.isSupported()) {
                // 然后尝试Log4j2
                adapter = new Log4j2Adapter();
                if (!adapter.isSupported()) {
                    throw new IllegalStateException("No supported logger implementation found");
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize logger adapter", e);
        }
        ADAPTER = adapter;
    }
    
    public static void log(LogEvent event) {
        ADAPTER.log(event);
    }
    
    public static void setLevel(String loggerName, LogLevel level) {
        ADAPTER.setLevel(loggerName, level);
    }
    
    public static LogLevel getLevel(String loggerName) {
        return ADAPTER.getLevel(loggerName);
    }
} 