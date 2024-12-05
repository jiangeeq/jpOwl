public class Log4j2Adapter implements LoggerAdapter {
    private final LoggerContext context;
    
    public Log4j2Adapter() {
        this.context = LogManager.getContext(false);
    }
    
    @Override
    public boolean isSupported() {
        try {
            Class.forName("org.apache.logging.log4j.core.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    @Override
    public void log(LogEvent event) {
        Logger logger = context.getLogger(event.getLoggerName());
        org.apache.logging.log4j.Level level = convertLevel(event.getLevel());
        logger.log(level, formatMessage(event));
    }
    
    @Override
    public void setLevel(String loggerName, LogLevel level) {
        LoggerConfig loggerConfig = context.getConfiguration().getLoggerConfig(loggerName);
        loggerConfig.setLevel(convertLevel(level));
        context.updateLoggers();
    }
    
    private org.apache.logging.log4j.Level convertLevel(LogLevel level) {
        return switch (level) {
            case TRACE -> org.apache.logging.log4j.Level.TRACE;
            case DEBUG -> org.apache.logging.log4j.Level.DEBUG;
            case INFO -> org.apache.logging.log4j.Level.INFO;
            case WARN -> org.apache.logging.log4j.Level.WARN;
            case ERROR -> org.apache.logging.log4j.Level.ERROR;
            default -> org.apache.logging.log4j.Level.INFO;
        };
    }
} 