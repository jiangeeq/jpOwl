package com.youpeng.jpowl.core.output;

import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.enums.OutputSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 输出源抽象基类
 */
public abstract class AbstractOutputSource implements OutputSource {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final Map<String, Object> properties;
    
    protected AbstractOutputSource(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    protected <T> T getProperty(String key, T defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            @SuppressWarnings("unchecked")
            T result = (T) value;
            return result;
        } catch (ClassCastException e) {
            logger.warn("Invalid property value type for key: " + key, e);
            return defaultValue;
        }
    }
} 