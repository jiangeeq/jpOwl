package com.youpeng.jpowl.core;

import com.youpeng.jpowl.core.monitor.MonitorManager;
import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.config.MonitorConfig;
import com.youpeng.jpowl.core.context.MonitorContext;

/**
 * JpOwl核心类
 */
public class JpOwlCore implements AutoCloseable {
    private final MonitorManager monitorManager;
    private final MonitorConfig config;
    private volatile boolean running = true;

    public JpOwlCore(OutputSource outputSource, MonitorConfig config) {
        if (outputSource == null) {
            throw new IllegalArgumentException("OutputSource cannot be null");
        }
        if (config == null) {
            throw new IllegalArgumentException("MonitorConfig cannot be null");
        }
        this.monitorManager = new MonitorManager(outputSource);
        this.config = config;
    }

    public MonitorContext startMonitor(String name) {
        if (!running || !config.isEnabled()) {
            return null;
        }
        return monitorManager.startMonitor(name);
    }

    @Override
    public void close() {
        if (running) {
            running = false;
            monitorManager.shutdown();
        }
    }
} 