package com.youpeng.jpowl.core.alert;

import com.youpeng.jpowl.core.api.AlertTrigger;
import com.youpeng.jpowl.core.spi.AlertTriggerProvider;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class AlertTriggerFactory {
    private static final Map<String, AlertTriggerProvider> PROVIDERS = 
        new ConcurrentHashMap<>();
        
    static {
        ServiceLoader<AlertTriggerProvider> loader = ServiceLoader.load(AlertTriggerProvider.class);
        for (AlertTriggerProvider provider : loader) {
            PROVIDERS.put(provider.getType(), provider);
        }
    }
    
    public static AlertTrigger createTrigger(String type, Map<String, Object> properties) {
        AlertTriggerProvider provider = PROVIDERS.get(type);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported alert trigger type: " + type);
        }
        return provider.createTrigger(properties);
    }
} 