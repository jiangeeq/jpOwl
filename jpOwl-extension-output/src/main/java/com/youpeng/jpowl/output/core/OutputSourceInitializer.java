package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.exception.OutputSourceException;
import com.youpeng.jpowl.output.manager.OutputSourceStatusManager;
import com.youpeng.jpowl.output.model.OutputSourceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 输出源初始化器
 */
public class OutputSourceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceInitializer.class);

    /**
     * 初始化输出源
     */
    public static void initialize(OutputSource outputSource) {
        OutputSourceType type = outputSource.getType();
        try {
            // 更新状态为初始化中
            OutputSourceStatusManager.updateStatus(type, OutputSourceStatus.Status.CREATED);
            
            // 执行初始化
            doInitialize(outputSource);
            
            // 更新状态为运行中
            OutputSourceStatusManager.updateStatus(type, OutputSourceStatus.Status.RUNNING);

            logger.info("Successfully initialized output source: {}", type);
        } catch (Exception e) {
            // 更新状态为错误
            OutputSourceStatusManager.updateStatus(type, OutputSourceStatus.Status.ERROR);
            throw new OutputSourceException(type, "Failed to initialize output source", e);
        }
    }
    
    private static void doInitialize(OutputSource outputSource) {
        // 这里可以添加具体的初始化逻辑
        // 比如检查连接、创建必要的资源等
    }
} 