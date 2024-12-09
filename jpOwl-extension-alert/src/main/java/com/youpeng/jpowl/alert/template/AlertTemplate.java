package com.youpeng.jpowl.alert.template;

import com.youpeng.jpowl.alert.model.MonitorModel;
import java.util.HashMap;
import java.util.Map;

/**
 * 告警消息模板
 * 用于统一各种告警方式的消息格式
 */
public class AlertTemplate {
    private String titleTemplate;
    private String contentTemplate;
    
    private AlertTemplate(Builder builder) {
        this.titleTemplate = builder.titleTemplate;
        this.contentTemplate = builder.contentTemplate;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * 获取邮件告警内容
     */
    public String getEmailContent(MonitorModel model) {
        return String.format(
    "告警级别：%s\n" +
    "告警时间：%s\n" +
    "告警源：%s\n" +
    "告警内容：%s\n",
    model.getLevel(),
    model.getTimestamp(),
    model.getSource(),
    model.getContent()
);

    }
    
    /**
     * 获取钉钉告警内容
     */
    public String getDingTalkContent(MonitorModel model) {
        return String.format("### %s\n%s", 
            processTemplate(titleTemplate, model),
            processTemplate(contentTemplate, model));
    }
    
    private String processTemplate(String template, MonitorModel model) {
        return template
            .replace("${name}", model.getTitle())
            .replace("${time}", model.getTimestamp())
            .replace("${content}", model.getContent())
            .replace("${level}", model.getLevel())
            .replace("${source}", model.getSource());
    }
    
    /**
     * 获取Webhook告警内容
     */
    public Map<String, Object> getWebhookContent(MonitorModel model) {
        Map<String, Object> content = new HashMap<>();
        content.put("level", model.getLevel());
        content.put("title", model.getTitle());
        content.put("content", model.getContent());
        content.put("timestamp", model.getTimestamp());
        content.put("source", model.getSource());
        content.put("tags", model.getTags());
        return content;
    }
    
    public static class Builder {
        private String titleTemplate = "【${level}】${name}";
        private String contentTemplate = 
            "#### 告警详情\n" +
            "- 告警级别：${level}\n" +
            "- 告警时间：${time}\n" +
            "- 告警源：${source}\n" +
            "- 告警内容：${content}\n" +
            "\n" +
            "---\n" +
            "请及时处理";
        
        public Builder titleTemplate(String titleTemplate) {
            this.titleTemplate = titleTemplate;
            return this;
        }
        
        public Builder contentTemplate(String contentTemplate) {
            this.contentTemplate = contentTemplate;
            return this;
        }
        
        public AlertTemplate build() {
            return new AlertTemplate(this);
        }
    }
} 