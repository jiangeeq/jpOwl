@Data
@Builder
public class LogEvent {
    private String loggerName;
    private LogLevel level;
    private String message;
    private Object[] args;
    private Throwable throwable;
    private long timestamp;
    private String threadName;
    private Map<String, String> mdc;
    
    public String formatMessage() {
        if (args != null && args.length > 0) {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        return message;
    }
} 