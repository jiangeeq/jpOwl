package com.youpeng.jpowl.processor;

import com.youpeng.jpowl.model.MonitorModel;

public interface MonitorProcessor {
    void process(MonitorModel model);
    void shutdown();
} 