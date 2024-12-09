package com.youpeng.jpowl.output.core.config;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.exception.OutputSourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 输出源配置管理器
 */
public class OutputSourceConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceConfigManager.class);

    private static final Map<OutputSourceType, OutputSourceConfig> configMap = new ConcurrentHashMap<>();
    
    /**
     * 注册配置
     */
    public static void registerConfig(OutputSourceConfig config) {
        try {
            config.validate();
            configMap.put(config.getType(), config);
            logger.info("Registered config for output source: {}", config.getType());
        } catch (Exception e) {
            throw new OutputSourceException(config.getType(), "Invalid configuration", e);
        }
    }
    
    /**
     * 获取配置
     */
    public static OutputSourceConfig getConfig(OutputSourceType type) {
        OutputSourceConfig config = configMap.get(type);
        if (config == null) {
            throw new OutputSourceException(type, "No configuration found");
        }
        return config;
    }
    
    /**
     * 更新配置
     */
    public static void updateConfig(OutputSourceConfig config) {
        config.validate();
        configMap.put(config.getType(), config);
        logger.info("Updated config for output source: {}", config.getType());
    }
    
    /**
     * 移除配置
     */
    public static void removeConfig(OutputSourceType type) {
        configMap.remove(type);
        logger.info("Removed config for output source: {}", type);
    }
    
    /**
     * 检查配置是否存在
     */
    public static boolean hasConfig(OutputSourceType type) {
        return configMap.containsKey(type);
    }
} 