package com.youpeng.jpowl.alert;

import java.util.ArrayList;

public class AlertExtension {
    private final AlertConfig config;
    private final List<AlertTrigger> triggers = new ArrayList<>();
    
    public AlertExtension(AlertConfig config) {
        this.config = config;
        initializeTriggers();
    }
    
    private void initializeTriggers() {
        if (config.isEmailEnabled()) {
            triggers.add(new EmailAlertTrigger(config.getEmailConfig()));
        }
        if (config.isDingTalkEnabled()) {
            triggers.add(new DingTalkAlertTrigger(config.getDingTalkConfig()));
        }
        if (config.isWebhookEnabled()) {
            triggers.add(new WebhookAlertTrigger(config.getWebhookConfig()));
        }
    }
    
    public void handleAlert(MonitorModel model) {
        CompletableFuture.runAsync(() -> {
            for (AlertTrigger trigger : triggers) {
                try {
                    trigger.execute(model);
                } catch (Exception e) {
                    log.error("Alert trigger execution failed", e);
                }
            }
        });
    }
} 