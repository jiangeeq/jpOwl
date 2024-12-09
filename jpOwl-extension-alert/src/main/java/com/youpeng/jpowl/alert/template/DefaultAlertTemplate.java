package com.youpeng.jpowl.alert.template;

import com.youpeng.jpowl.core.api.AlertTemplate;
import com.youpeng.jpowl.core.model.MonitorModel;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认告警模板实现
 */
public class DefaultAlertTemplate implements AlertTemplate {
    
    @Override
    public String getEmailContent(MonitorModel model) {
        return String.format("""
            告警标题: %s
            告警级别: %s
            告警内容: %s
            告警时间: %s
            告警来源: %s
            """,
            model.getTitle(),
            model.getLevel(),
            model.getContent(),
            model.getTimestamp(),
            model.getSource()
        );
    }
    
    @Override
    public String getDingTalkContent(MonitorModel model) {
        return String.format("""
            ### %s
            > **级别**: %s  
            > **时间**: %s  
            > **来源**: %s  
            
            **详情**:  
            %s
            """,
            model.getTitle(),
            model.getLevel(),
            model.getTimestamp(),
            model.getSource(),
            model.getContent()
        );
    }
    
    @Override
    public Map<String, Object> getWebhookContent(MonitorModel model) {
        Map<String, Object> content = new HashMap<>();
        content.put("title", model.getTitle());
        content.put("level", model.getLevel());
        content.put("content", model.getContent());
        content.put("timestamp", model.getTimestamp());
        content.put("source", model.getSource());
        return content;
    }
} 