package com.youpeng.jpowl.output.manager;

import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.exception.OutputException;
import com.youpeng.jpowl.output.core.OutputSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 输出源生命周期管理器
 */
public class OutputSourceLifecycleManager {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceLifecycleManager.class);

    private final Map<OutputSourceType, OutputSource> activeSources = new ConcurrentHashMap<>();
    
    /**
     * 启动输出源
     */
    public void start(OutputSourceType type, OutputSource source) {
        try {
            activeSources.put(type, source);
            logger.info("Started output source: {}", type);
        } catch (Exception e) {
            throw new OutputException("Failed to start output source: " + type, e);
        }
    }
    
    /**
     * 停止输出源
     */
    public void stop(OutputSourceType type) {
        OutputSource source = activeSources.remove(type);
        if (source != null) {
            try {
                source.close();
                logger.info("Stopped output source: {}", type);
            } catch (Exception e) {
                logger.error("Error stopping output source: {}", type, e);
            }
        }
    }
    
    /**
     * 停止所有输出源
     */
    public void stopAll() {
        activeSources.forEach((type, source) -> {
            try {
                source.close();
                logger.info("Stopped output source: {}", type);
            } catch (Exception e) {
                logger.error("Error stopping output source: {}", type, e);
            }
        });
        activeSources.clear();
    }
    
    /**
     * 获取活跃的输出源
     */
    public OutputSource getActiveSource(OutputSourceType type) {
        return activeSources.get(type);
    }
    
    /**
     * 检查输出源是否活跃
     */
    public boolean isActive(OutputSourceType type) {
        return activeSources.containsKey(type);
    }
} 