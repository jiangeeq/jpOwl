package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.model.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 输出源抽象基类
 * 提供通用实现和工具方法
 */
public abstract class AbstractOutputSource implements OutputSource {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final Map<String, Object> properties;
    
    protected AbstractOutputSource(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    /**
     * 批量写入的默认实现
     * 子类可以覆盖此方法提供更高效的实现
     */
    @Override
    public void writeBatch(List<MonitorData> dataList) {
        for (MonitorData data : dataList) {
            write(data);
        }
    }
    
    /**
     * 获取配置属性值，支持默认值
     */
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
    
    /**
     * 默认的关闭实现
     */
    @Override
    public void close() {
        // 默认空实现，子类根据需要覆盖
    }
} 