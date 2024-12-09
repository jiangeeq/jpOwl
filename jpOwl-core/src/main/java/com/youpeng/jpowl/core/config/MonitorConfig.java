package com.youpeng.jpowl.core.config;

import com.youpeng.jpowl.core.constants.CoreConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * 监控配置类
 * 用于管理监控相关的配置项
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MonitorConfig {
    private boolean enabled = true;                    // 是否启用监控
    private int samplingRate = CoreConstants.DEFAULT_SAMPLING_RATE;  // 采样率
    private int bufferSize = CoreConstants.DEFAULT_BUFFER_SIZE;      // 缓冲区大小
    private String outputType = "FILE";                // 输出类型
    private String outputPath = CoreConstants.DEFAULT_LOG_PATH;      // 输出路径
    private final Map<String, String> properties = new HashMap<>();  // 扩展属性

    /**
     * 配置构建器
     */
    public static class Builder {
        private final MonitorConfig config = new MonitorConfig();

        /**
         * 设置是否启用监控
         *
         * @param enabled 是否启用
         * @return 构建器实例
         */
        public Builder enabled(boolean enabled) {
            config.enabled = enabled;
            return this;
        }

        /**
         * 设置采样率
         *
         * @param samplingRate 采样率(0-100)
         * @return 构建器实例
         */
        public Builder samplingRate(int samplingRate) {
            config.samplingRate = samplingRate;
            return this;
        }

        /**
         * 设置缓冲区大小
         *
         * @param bufferSize 缓冲区大小
         * @return 构建器实例
         */
        public Builder bufferSize(int bufferSize) {
            config.bufferSize = bufferSize;
            return this;
        }

        /**
         * 设置输出类型
         *
         * @param outputType 输出类型
         * @return 构建器实例
         */
        public Builder outputType(String outputType) {
            config.outputType = outputType;
            return this;
        }

        /**
         * 设置输出路径
         *
         * @param outputPath 输出路径
         * @return 构建器实例
         */
        public Builder outputPath(String outputPath) {
            config.outputPath = outputPath;
            return this;
        }

        /**
         * 构建配置实例
         *
         * @return 配置实例
         */
        public MonitorConfig build() {
            return config;
        }
    }

    // Getters
    public boolean isEnabled() { return enabled; }
    public int getSamplingRate() { return samplingRate; }
    public int getBufferSize() { return bufferSize; }
    public String getOutputType() { return outputType; }
    public String getOutputPath() { return outputPath; }

    /**
     * 设置扩展属性
     *
     * @param key 属性键
     * @param value 属性值
     */
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    /**
     * 获取扩展属性
     *
     * @param key 属性键
     * @return 属性值
     */
    public String getProperty(String key) {
        return properties.get(key);
    }

    /**
     * 获取扩展属性，如果不存在则返回默认值
     *
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
} 