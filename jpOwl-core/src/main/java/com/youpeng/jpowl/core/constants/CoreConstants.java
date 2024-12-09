package com.youpeng.jpowl.core.constants;

/**
 * 核心常量类
 * 定义了框架中使用的各种常量
 * 
 * @author youpeng
 * @since 1.0.0
 */
public final class CoreConstants {
    
    private CoreConstants() {}  // 防止实例化
    
    /**
     * 默认配置常量
     */
    public static final String DEFAULT_LOG_PATH = "logs/monitor.log";
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final int DEFAULT_BATCH_SIZE = 100;
    public static final long DEFAULT_FLUSH_INTERVAL = 1000L;
    public static final int DEFAULT_QUEUE_SIZE = 10000;
    
    /**
     * 时间相关常量(毫秒)
     */
    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    
    /**
     * 监控相关常量
     */
    public static final int DEFAULT_SAMPLING_RATE = 100;
    public static final long DEFAULT_TIMEOUT = 5000L;
    public static final String UNKNOWN_SOURCE = "unknown";
    
    /**
     * 系统属性键
     */
    public static final String PROP_APP_NAME = "app.name";
    public static final String PROP_LOG_PATH = "jpowl.log.path";
    public static final String PROP_BUFFER_SIZE = "jpowl.buffer.size";
} 