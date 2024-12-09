package com.youpeng.jpowl.alert;

import com.youpeng.jpowl.alert.impl.AlertTrigger;
import com.youpeng.jpowl.alert.impl.DingTalkAlertTrigger;
import com.youpeng.jpowl.alert.impl.WebhookAlertTrigger;
import com.youpeng.jpowl.alert.limiter.AlertRateLimiter;
import com.youpeng.jpowl.alert.model.AlertConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.youpeng.jpowl.alert.impl.EmailAlertTrigger;
import com.youpeng.jpowl.alert.model.MonitorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 告警扩展模块核心类
 */
public class AlertExtension {
    private static final Logger log = LoggerFactory.getLogger(AlertExtension.class);
    private final AlertConfig config;
    private final List<AlertTrigger> triggers;
    private final AlertRateLimiter rateLimiter;

    public AlertExtension(AlertConfig config) {
        this.config = config;
        this.triggers = new ArrayList<>();
        this.rateLimiter = new AlertRateLimiter(
            config.getMaxAlertsPerWindow(),
            config.getTimeWindowSeconds()
        );
        initializeTriggers();
    }
    
    /**
     * 初始化已配置的告警触发器
     */
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
    
    /**
     * 处理告警事件
     * 异步执行各个告警触发器
     * @param model 监控数据模型
     */
    public void handleAlert(MonitorModel model) {
        // 生成告警key
        String alertKey = generateAlertKey(model);
        
        // 检查限流
        if (!rateLimiter.allowAlert(alertKey)) {
            log.warn("告警被限流: {}", alertKey);
            return;
        }
        
        // 异步处理告警
        CompletableFuture.runAsync(() -> {
            // 触发告警通知
            for (AlertTrigger trigger : triggers) {
                try {
                    trigger.execute(model);
                } catch (Exception e) {
                    log.error("告警触发执行失败: {}", trigger.getClass().getSimpleName(), e);
                }
            }
        });
    }
    
    private String generateAlertKey(MonitorModel model) {
        return String.format("%s_%s_%s",
            model.getSource(),
            model.getLevel(),
            model.getTitle()
        );
    }
} 