/**
 * JpOwl常量定义类
 */
public class JpOwlConstants {
    
    public static final String PROPERTY_PREFIX = "jpowl";
    
    public static class Output {
        public static final String REDIS_PREFIX = PROPERTY_PREFIX + ".output.redis";
        public static final String MYSQL_PREFIX = PROPERTY_PREFIX + ".output.mysql";
    }
    
    public static class Monitor {
        public static final String PREFIX = PROPERTY_PREFIX + ".monitor";
        public static final String ENABLED = PREFIX + ".enabled";
    }
    
    public static class Trigger {
        public static final String PREFIX = PROPERTY_PREFIX + ".trigger";
        public static final String ENABLED = PREFIX + ".enabled";
    }
} 