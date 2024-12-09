package com.youpeng.jpowl.alert.impl;

import com.youpeng.jpowl.alert.exception.AlertException;
import com.youpeng.jpowl.alert.model.MonitorModel;

/**
 * 告警触发器接口
 * 定义告警执行的标准接口
 */
public interface AlertTrigger {
    /**
     * 执行告警
     * @param model 监控数据模型
     * @throws AlertException 告警执行异常
     */
    void execute(MonitorModel model) throws AlertException;
} 