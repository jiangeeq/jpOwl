package com.youpeng.jpowl.alert.model;


import java.util.Map;

/**
 * 告警配置类
 */
public class AlertConfig {
    private boolean emailEnabled;
    private boolean dingTalkEnabled; 
    private boolean webhookEnabled;
    
    private EmailConfig emailConfig;
    private DingTalkConfig dingTalkConfig;
    private WebhookConfig webhookConfig;

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isDingTalkEnabled() {
        return dingTalkEnabled;
    }

    public void setDingTalkEnabled(boolean dingTalkEnabled) {
        this.dingTalkEnabled = dingTalkEnabled;
    }

    public boolean isWebhookEnabled() {
        return webhookEnabled;
    }

    public void setWebhookEnabled(boolean webhookEnabled) {
        this.webhookEnabled = webhookEnabled;
    }

    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public DingTalkConfig getDingTalkConfig() {
        return dingTalkConfig;
    }

    public void setDingTalkConfig(DingTalkConfig dingTalkConfig) {
        this.dingTalkConfig = dingTalkConfig;
    }

    public WebhookConfig getWebhookConfig() {
        return webhookConfig;
    }

    public void setWebhookConfig(WebhookConfig webhookConfig) {
        this.webhookConfig = webhookConfig;
    }
}

