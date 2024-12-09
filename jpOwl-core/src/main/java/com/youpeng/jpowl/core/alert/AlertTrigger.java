package com.youpeng.jpowl.core.alert;

import com.youpeng.jpowl.core.model.MonitorModel;
import com.youpeng.jpowl.core.exception.AlertException;

/**
 * 告警触发器接口
 */
public interface AlertTrigger {
    /**
     * 执行告警
     */
    void execute(MonitorModel model) throws AlertException;
} 