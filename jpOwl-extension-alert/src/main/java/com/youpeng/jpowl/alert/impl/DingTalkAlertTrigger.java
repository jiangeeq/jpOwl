package com.youpeng.jpowl.alert.impl;

import com.youpeng.jpowl.core.api.AlertTrigger;
import com.youpeng.jpowl.core.api.AlertTemplate;
import com.youpeng.jpowl.core.model.MonitorModel;
import com.youpeng.jpowl.core.exception.AlertException;
import com.youpeng.jpowl.alert.config.DingTalkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 钉钉告警触发器实现
 */
public class DingTalkAlertTrigger implements AlertTrigger {
    private static final Logger logger = LoggerFactory.getLogger(DingTalkAlertTrigger.class);
    private static final String TYPE = "dingtalk";

    private final DingTalkConfig config;
    private final AlertTemplate template;
    
    public DingTalkAlertTrigger(DingTalkConfig config, AlertTemplate template) {
        this.config = config;
        this.template = template;
    }
    
    @Override
    public void execute(MonitorModel model) throws AlertException {
        try {
            String content = template.getDingTalkContent(model);
            // 发送钉钉消息的具体实现
            logger.info("Sending DingTalk alert: {}", model);
        } catch (Exception e) {
            throw new AlertException("Failed to send DingTalk alert", e);
        }
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
} 