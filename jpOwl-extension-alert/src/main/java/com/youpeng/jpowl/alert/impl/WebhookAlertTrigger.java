package com.youpeng.jpowl.alert.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youpeng.jpowl.alert.exception.AlertException;
import com.youpeng.jpowl.alert.model.MonitorModel;
import com.youpeng.jpowl.alert.model.WebhookConfig;
import com.youpeng.jpowl.alert.template.AlertTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Webhook告警实现
 */

public class WebhookAlertTrigger implements AlertTrigger {
    private static final Logger logger = LoggerFactory.getLogger(WebhookAlertTrigger.class);

    private final WebhookConfig config;
    private final AlertTemplate template;
    private final ObjectMapper objectMapper;
    
    public WebhookAlertTrigger(WebhookConfig config) {
        this.config = config;
        this.template = AlertTemplate.builder().build();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void execute(MonitorModel model) throws AlertException {
        try {
            Map<String, Object> content = template.getWebhookContent(model);
            String jsonBody = objectMapper.writeValueAsString(content);
            
            // TODO: 实现HTTP请求发送逻辑

            logger.info("发送Webhook告警: {}", model);
        } catch (Exception e) {
            throw new AlertException("Webhook告警发送失败", e);
        }
    }
} 