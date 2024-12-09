package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.exception.OutputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 输出源构建器
 */
public class OutputSourceBuilder {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceBuilder.class);

    private final OutputSourceType type;
    private final Map<String, Object> properties = new HashMap<>();
    
    private OutputSourceBuilder(OutputSourceType type) {
        this.type = type;
    }
    
    public static OutputSourceBuilder create(OutputSourceType type) {
        return new OutputSourceBuilder(type);
    }
    
    public OutputSourceBuilder property(String key, Object value) {
        properties.put(key, value);
        return this;
    }
    
    public OutputSourceBuilder properties(Map<String, Object> props) {
        properties.putAll(props);
        return this;
    }
    
    public OutputSource build() {
        try {
            Class<? extends OutputSource> sourceClass = OutputSourceRegistry.getImplementation(type);
            return sourceClass.getConstructor(Map.class).newInstance(properties);
        } catch (Exception e) {
            logger.error("Failed to build output source: {}", type, e);
            throw new OutputException("Failed to build output source", e);
        }
    }
} 