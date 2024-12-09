package com.youpeng.jpowl.output.core.config;

import com.youpeng.jpowl.output.core.OutputSourceType;

/**
 * 输出源配置基类
 */
/**
 * 抽象类OutputSourceConfig用于配置输出源的相关参数
 * 它定义了一些通用的配置属性和一个抽象方法，用于验证配置的正确性
 */
public abstract class OutputSourceConfig {
    // 输出源的类型，一旦创建不可更改
    private final OutputSourceType type;
    // 表示输出源是否启用，默认为true
    private boolean enabled = true;
    // 每次处理的数据量，默认为1000
    private int batchSize = 1000;
    // 队列的最大容量，默认为10000
    private int queueCapacity = 10000;
    // 失败后的重试次数，默认为3次
    private int retryTimes = 3;

    public OutputSourceType getType() {
        return type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     * 构造方法，初始化OutputSourceConfig实例
     *
     * @param type OutputSourceType类型的参数，用于指定输出源的类型
     */
    protected OutputSourceConfig(OutputSourceType type) {
        this.type = type;
    }

    /**
     * 抽象方法validate用于验证当前配置是否有效
     * 子类必须实现此方法以提供具体的验证逻辑
     */
    public abstract void validate();
}
