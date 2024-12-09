package com.youpeng.jpowl.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JpOwl 配置属性类
 */
@ConfigurationProperties(prefix = "jpowl")
public class JpOwlProperties {
    
    /**
     * 是否启用 JpOwl
     */
    private boolean enabled = true;

    /**
     * 输出源类型：MYSQL, REDIS
     */
    private String outputSourceType;
    
    /**
     * 重试次数
     */
    private int retryTimes = 3;

    // getter and setter methods
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getOutputSourceType() {
        return outputSourceType;
    }

    public void setOutputSourceType(String outputSourceType) {
        this.outputSourceType = outputSourceType;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
} 