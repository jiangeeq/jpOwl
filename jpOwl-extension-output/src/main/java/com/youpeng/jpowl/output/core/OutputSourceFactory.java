package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.provider.elasticsearch.ElasticsearchOutputSource;
import com.youpeng.jpowl.output.provider.influxdb.InfluxDBOutputSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 输出源工厂类
 * 负责创建和管理不同类型的输出源
 */
public class OutputSourceFactory {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceFactory.class);
    private static final Map<OutputSourceType, Class<? extends OutputSource>> OUTPUT_SOURCES = new HashMap<>();
    
    static {
        // 注册支持的输出源类型
        OUTPUT_SOURCES.put(OutputSourceType.ELASTICSEARCH, ElasticsearchOutputSource.class);
        OUTPUT_SOURCES.put(OutputSourceType.INFLUXDB, InfluxDBOutputSource.class);
        OUTPUT_SOURCES.put(OutputSourceType.FILE, null);
        // 在这里注册其他输出源类型
    }
    
    /**
     * 创建输出源实例
     * @param type 输出源类型
     * @param properties 配置属性
     * @return 输出源实例
     */
    public static OutputSource createOutputSource(OutputSourceType type, Map<String, Object> properties) {
        Class<? extends OutputSource> sourceClass = OUTPUT_SOURCES.get(type);
        if (sourceClass == null) {
            throw new IllegalArgumentException("Unsupported output source type: " + type);
        }
        
        try {
            Constructor<? extends OutputSource> constructor = sourceClass.getConstructor(Map.class);
            return constructor.newInstance(properties);
        } catch (Exception e) {
            logger.error("Failed to create output source for type: " + type, e);
            throw new RuntimeException("Failed to create output source", e);
        }
    }
} 