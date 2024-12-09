package com.youpeng.jpowl.alert.provider;

import com.youpeng.jpowl.core.spi.AlertTriggerProvider;
import com.youpeng.jpowl.core.api.AlertTrigger;
import com.youpeng.jpowl.alert.impl.DingTalkAlertTrigger;
import com.youpeng.jpowl.alert.config.DingTalkConfig;
import com.youpeng.jpowl.alert.template.DefaultAlertTemplate;
import java.util.Map;

public class DingTalkAlertTriggerProvider implements AlertTriggerProvider {
    private static final String TYPE = "dingtalk";
    
    @Override
    public AlertTrigger createTrigger(Map<String, Object> properties) {
        DingTalkConfig config = new DingTalkConfig();
        config.setWebhook((String) properties.get("webhook"));
        config.setSecret((String) properties.get("secret"));
        return new DingTalkAlertTrigger(config, new DefaultAlertTemplate());
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
} 