package com.youpeng.jpowl.output.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 输出源注册器
 */
public class OutputSourceRegistry {
    protected static final Logger logger = LoggerFactory.getLogger(OutputSourceRegistry.class);

    private static final Map<OutputSourceType, Class<? extends OutputSource>> REGISTRY = new ConcurrentHashMap<>();
    
    /**
     * 注册输出源实现
     */
    public static void register(OutputSourceType type, Class<? extends OutputSource> sourceClass) {
        REGISTRY.put(type, sourceClass);
        logger.info("Registered output source implementation: {} -> {}", type, sourceClass.getName());
    }
    
    /**
     * 获取输出源实现类
     */
    public static Class<? extends OutputSource> getImplementation(OutputSourceType type) {
        Class<? extends OutputSource> sourceClass = REGISTRY.get(type);
        if (sourceClass == null) {
            throw new IllegalArgumentException("No implementation found for output source type: " + type);
        }
        return sourceClass;
    }
    
    /**
     * 检查是否支持指定类型
     */
    public static boolean isSupported(OutputSourceType type) {
        return REGISTRY.containsKey(type);
    }
} 