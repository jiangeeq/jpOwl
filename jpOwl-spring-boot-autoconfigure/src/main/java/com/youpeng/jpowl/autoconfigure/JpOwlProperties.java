@ConfigurationProperties(prefix = "jpowl")
public class JpOwlProperties {
    private String logPrefix = "jpowl";
    private LogLevel logLevel = LogLevel.INFO;
    private Trigger trigger = new Trigger();
    
    public static class Trigger {
        private boolean enabled = false;
        private String type = "email";
        // getters and setters
    }
    // getters and setters
} 