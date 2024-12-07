package com.youpeng.jpowl.logging.filter;

import com.youpeng.jpowl.logging.model.LogEvent;

import java.util.Random;

/**
 * 采样过滤器
 * 按比例对日志进行采样
 */
public class SamplingFilter implements LogFilter {
    private final int samplingRate; // 1-100
    private final Random random = new Random();
    
    public SamplingFilter(int samplingRate) {
        this.samplingRate = Math.min(100, Math.max(1, samplingRate));
    }
    
    @Override
    public boolean accept(LogEvent event) {
        return random.nextInt(100) < samplingRate;
    }
    
    @Override
    public LogEvent process(LogEvent event) {
        return event;
    }
} 