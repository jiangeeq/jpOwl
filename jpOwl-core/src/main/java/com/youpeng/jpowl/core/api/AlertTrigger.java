package com.youpeng.jpowl.core.api;

import com.youpeng.jpowl.core.model.MonitorModel;
import com.youpeng.jpowl.core.exception.AlertException;

/**
 * 告警触发器接口
 * 定义了告警的触发行为
 * 
 * @author youpeng
 * @since 1.0.0
 */
public interface AlertTrigger {
    /**
     * 执行告警
     * 
     * @param model 监控模型，包含告警相关信息
     * @throws AlertException 当告警执行失败时抛出
     */
    void execute(MonitorModel model) throws AlertException;
    
    /**
     * 获取告警触发器类型
     * 
     * @return 告警触发器类型标识
     */
    String getType();
} 