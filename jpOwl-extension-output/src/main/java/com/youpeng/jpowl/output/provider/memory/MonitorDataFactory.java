package com.youpeng.jpowl.output.provider.memory;

import com.lmax.disruptor.EventFactory;
import com.youpeng.jpowl.output.model.MonitorData;

public class MonitorDataFactory implements EventFactory<MonitorData> {
    @Override
    public MonitorData newInstance() {
        return new MonitorData();
    }
} 