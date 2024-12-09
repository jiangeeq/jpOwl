/**
 * JpOwl配置属性类
 */
@ConfigurationProperties(prefix = "jpowl")
public class JpOwlProperties {
    
    private String logPrefix;
    private String logLevel;
    private TriggerProperties trigger = new TriggerProperties();
    private OutputProperties output = new OutputProperties();
    
    // getter and setter

    /**
     * 告警触发器配置
     */
    public static class TriggerProperties {
        private boolean enabled;
        private String type;
        // getter and setter
    }

    /**
     * 输出配置
     */
    public static class OutputProperties {
        private RedisProperties redis = new RedisProperties();
        private MySQLProperties mysql = new MySQLProperties();
        // getter and setter
    }

    /**
     * Redis输出配置
     */
    public static class RedisProperties {
        private boolean enabled;
        private String host;
        private int port;
        // getter and setter
    }

    /**
     * MySQL输出配置
     */
    public static class MySQLProperties {
        private boolean enabled;
        private String url;
        private String username;
        private String password;
        // getter and setter
    }
} 