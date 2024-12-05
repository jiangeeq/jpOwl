package com.youpeng.jpowl.spring.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "jpowl")
public class JpOwlEndpoint {
    
    private final JpOwlCore jpOwlCore;
    private final JpOwlProperties properties;
    
    public JpOwlEndpoint(JpOwlCore jpOwlCore, JpOwlProperties properties) {
        this.jpOwlCore = jpOwlCore;
        this.properties = properties;
    }
    
    @ReadOperation
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("enabled", properties.isEnabled());
        info.put("outputSources", getOutputSourcesInfo());
        info.put("monitorStats", getMonitorStats());
        return info;
    }
    
    @ReadOperation
    public Map<String, Object> metrics() {
        return jpOwlCore.getMetrics();
    }
    
    @WriteOperation
    public void updateSamplingRate(@Selector String name, @Param int rate) {
        if (rate >= 0 && rate <= 100) {
            properties.getMonitor().setSamplingRate(rate);
        } else {
            throw new IllegalArgumentException("Sampling rate must be between 0 and 100");
        }
    }
} 