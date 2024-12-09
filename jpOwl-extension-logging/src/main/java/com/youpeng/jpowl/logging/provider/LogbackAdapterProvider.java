package com.youpeng.jpowl.logging.provider;

import com.youpeng.jpowl.core.spi.LogAdapterProvider;
import com.youpeng.jpowl.core.api.LogAdapter;
import com.youpeng.jpowl.logging.adapter.LogbackAdapter;
import java.util.Map;

public class LogbackAdapterProvider implements LogAdapterProvider {
    private static final String TYPE = "logback";
    
    @Override
    public LogAdapter createAdapter(Map<String, Object> properties) {
        return new LogbackAdapter(properties);
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    @Override
    public boolean isSupported() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
} 