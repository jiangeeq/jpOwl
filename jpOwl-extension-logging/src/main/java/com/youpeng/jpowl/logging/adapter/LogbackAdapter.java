public class LogbackAdapter implements LoggerAdapter {
    private final LoggerContext loggerContext;
    
    public LogbackAdapter() {
        this.loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    }
    
    @Override
    public boolean isSupported() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    @Override
    public void log(LogEvent event) {
        Logger logger = loggerContext.getLogger(event.getLoggerName());
        Level level = convertLevel(event.getLevel());
        logger.log(level, formatMessage(event));
    }
    
    @Override
    public void setLevel(String loggerName, LogLevel level) {
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(convertLevel(level));
    }
    
    private Level convertLevel(LogLevel level) {
        return switch (level) {
            case TRACE -> Level.TRACE;
            case DEBUG -> Level.DEBUG;
            case INFO -> Level.INFO;
            case WARN -> Level.WARN;
            case ERROR -> Level.ERROR;
            default -> Level.INFO;
        };
    }
} 