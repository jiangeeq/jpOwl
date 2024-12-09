package com.youpeng.jpowl.core.logging;

import com.youpeng.jpowl.core.api.LogAdapter;
import com.youpeng.jpowl.core.spi.LogAdapterProvider;
import com.youpeng.jpowl.core.util.ServiceLoaderUtils;
import java.util.Map;

public class LogAdapterFactory {
    private static final Map<String, LogAdapterProvider> PROVIDERS = 
        ServiceLoaderUtils.loadServices(
            LogAdapterProvider.class,
            LogAdapterProvider::getType
        );
    
    public static LogAdapter createAdapter(String type, Map<String, Object> properties) {
        LogAdapterProvider provider = PROVIDERS.get(type);
        if (provider == null || !provider.isSupported()) {
            throw new IllegalArgumentException("Unsupported log adapter type: " + type);
        }
        return provider.createAdapter(properties);
    }
    
    public static LogAdapter autoDetectAdapter(Map<String, Object> properties) {
        return PROVIDERS.values().stream()
            .filter(LogAdapterProvider::isSupported)
            .findFirst()
            .map(provider -> provider.createAdapter(properties))
            .orElseThrow(() -> new IllegalStateException("No supported log adapter found"));
    }
} 