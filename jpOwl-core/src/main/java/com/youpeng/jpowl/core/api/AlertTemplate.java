package com.youpeng.jpowl.core.api;

import com.youpeng.jpowl.core.model.MonitorModel;
import java.util.Map;

/**
 * 告警模板接口
 */
public interface AlertTemplate {
    /**
     * 获取邮件内容
     */
    String getEmailContent(MonitorModel model);
    
    /**
     * 获取钉钉内容
     */
    String getDingTalkContent(MonitorModel model);
    
    /**
     * 获取Webhook内容
     */
    Map<String, Object> getWebhookContent(MonitorModel model);
} 