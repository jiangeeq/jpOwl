package com.youpeng.jpowl.output.core;

/**
 * 输出源类型枚举
 */
public enum OutputSourceType {
    /**
     * Elasticsearch输出源
     */
    ELASTICSEARCH("elasticsearch"),
    
    /**
     * InfluxDB输出源
     */
    INFLUXDB("influxdb"),

    REDIS("redis"),

    MYSQL("mysql"),
    MEMORY("memory"),
    /**
     * 文件输出源
     */
    FILE("file");
    
    private final String code;
    
    OutputSourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    /**
     * 根据代码获取类型
     */
    public static OutputSourceType fromCode(String code) {
        for (OutputSourceType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown output source type: " + code);
    }
} 