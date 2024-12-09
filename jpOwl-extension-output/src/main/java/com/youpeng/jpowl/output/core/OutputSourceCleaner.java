package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.exception.OutputSourceException;
import com.youpeng.jpowl.output.manager.OutputSourceStatusManager;
import com.youpeng.jpowl.output.model.OutputSourceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 输出源清理器
 */
public class OutputSourceCleaner {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceCleaner.class);

    /**
     * 清理输出源资源
     */
    public static void cleanup(OutputSource outputSource) {
        OutputSourceType type = outputSource.getType();
        try {
            logger.info("Starting cleanup for output source: {}", type);
            
            // 执行清理
            doCleanup(outputSource);
            
            // 更新状态为已停止
            OutputSourceStatusManager.updateStatus(type, OutputSourceStatus.Status.STOPPED);

            logger.info("Successfully cleaned up output source: {}", type);
        } catch (Exception e) {
            // 更新状态为错误
            OutputSourceStatusManager.updateStatus(type, OutputSourceStatus.Status.ERROR);
            throw new OutputSourceException(type, "Failed to cleanup output source", e);
        }
    }
    
    private static void doCleanup(OutputSource outputSource) {
        try {
            outputSource.close();
        } catch (Exception e) {
            throw new OutputSourceException(outputSource.getType(), "Error during cleanup", e);
        }
    }
} 