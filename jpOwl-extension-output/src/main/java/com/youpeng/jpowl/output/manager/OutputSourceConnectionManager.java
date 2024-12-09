package com.youpeng.jpowl.output.manager;

import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.core.OutputSourceFactory;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.exception.OutputSourceException;
import com.youpeng.jpowl.output.util.RetryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 输出源连接管理器
 */
public class OutputSourceConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceFactory.class);

    private static final Map<OutputSourceType, ConnectionInfo> connectionMap = new ConcurrentHashMap<>();
    
    /**
     * 建立连接
     */
    public static void connect(OutputSource outputSource) {
        OutputSourceType type = outputSource.getType();
        try {
            logger.info("Establishing connection for output source: {}", type);
            
            // 使用重试机制建立连接
            RetryUtil.executeWithRetry(() -> {
                doConnect(outputSource);
                return null;
            }, 3, 1000);
            
            // 记录连接信息
            connectionMap.put(type, new ConnectionInfo(outputSource));

            logger.info("Successfully established connection for output source: {}", type);
        } catch (Exception e) {
            throw new OutputSourceException(type, "Failed to establish connection", e);
        }
    }
    
    /**
     * 断开连接
     */
    public static void disconnect(OutputSourceType type) {
        ConnectionInfo info = connectionMap.remove(type);
        if (info != null) {
            try {
                info.getOutputSource().close();
                logger.info("Successfully disconnected output source: {}", type);
            } catch (Exception e) {
                logger.error("Error disconnecting output source: {}", type, e);
            }
        }
    }
    
    private static void doConnect(OutputSource outputSource) {
        // 具体的连接逻辑由各个输出源实现
        // 这里可以添加通用的连接检查逻辑
    }

    private static class ConnectionInfo {
        private final OutputSource outputSource;
        private final long connectedTime = System.currentTimeMillis();

        public ConnectionInfo(OutputSource outputSource) {
            this.outputSource = outputSource;
        }

        public OutputSource getOutputSource() {
            return outputSource;
        }

        public long getConnectedTime() {
            return connectedTime;
        }
    }
} 