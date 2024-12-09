package com.youpeng.jpowl.core.sampler;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 监控采样器
 * 用于控制监控数据的采样率，避免产生过多数据
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class MonitorSampler {
    private final int samplingRate;  // 采样率(0-100)

    /**
     * 构造采样器
     *
     * @param samplingRate 采样率，范围0-100
     * @throws IllegalArgumentException 如果采样率不在有效范围内
     */
    public MonitorSampler(int samplingRate) {
        if (samplingRate < 0 || samplingRate > 100) {
            throw new IllegalArgumentException("Sampling rate must be between 0 and 100");
        }
        this.samplingRate = samplingRate;
    }

    /**
     * 判断是否应该采样
     * 
     * @return true如果应该采样，false如果应该跳过
     */
    public boolean shouldSample() {
        if (samplingRate <= 0) return false;
        if (samplingRate >= 100) return true;
        
        return ThreadLocalRandom.current().nextInt(100) < samplingRate;
    }
} 