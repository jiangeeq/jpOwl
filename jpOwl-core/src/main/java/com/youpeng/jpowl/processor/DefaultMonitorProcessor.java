package com.youpeng.jpowl.processor;

import com.youpeng.jpowl.model.MonitorModel;
import com.youpeng.jpowl.output.OutputHandler;
import com.youpeng.jpowl.trigger.TriggerManager;

public class DefaultMonitorProcessor implements MonitorProcessor {
    private final OutputHandler outputHandler;
    private final TriggerManager triggerManager;

    public DefaultMonitorProcessor(OutputHandler outputHandler, TriggerManager triggerManager) {
        this.outputHandler = outputHandler;
        this.triggerManager = triggerManager;
    }

    @Override
    public void process(MonitorModel model) {
        // 写入输出
        outputHandler.write(model.serialize());
        // 触发告警
        triggerManager.fireTriggers(model);
    }

    @Override
    public void shutdown() {
        outputHandler.close();
        triggerManager.shutdown();
    }
} 