package com.youpeng.jpowl.alert.impl;

import com.youpeng.jpowl.alert.exception.AlertException;
import com.youpeng.jpowl.alert.model.EmailConfig;
import com.youpeng.jpowl.alert.model.MonitorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件告警实现
 */

public class EmailAlertTrigger implements AlertTrigger {
    private static final Logger logger = LoggerFactory.getLogger(EmailAlertTrigger.class);

    private final EmailConfig config;
    
    public EmailAlertTrigger(EmailConfig config) {
        this.config = config;
    }
    
    @Override
    public void execute(MonitorModel model) throws AlertException {
        try {
            // 邮件发送逻辑实现
            logger.info("发送邮件告警: {}", model);
        } catch (Exception e) {
            throw new AlertException("邮件告警发送失败", e);
        }
    }
} 