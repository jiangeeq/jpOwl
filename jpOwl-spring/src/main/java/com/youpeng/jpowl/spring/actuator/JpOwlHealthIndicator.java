package com.youpeng.jpowl.spring.actuator;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JpOwlHealthIndicator extends AbstractHealthIndicator {
    
    private final JpOwlCore jpOwlCore;
    private final OutputSourceManager outputSourceManager;
    
    public JpOwlHealthIndicator(JpOwlCore jpOwlCore, OutputSourceManager outputSourceManager) {
        this.jpOwlCore = jpOwlCore;
        this.outputSourceManager = outputSourceManager;
    }
    
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        if (!jpOwlCore.isRunning()) {
            builder.down()
                   .withDetail("error", "JpOwl core is not running");
            return;
        }
        
        Map<String, Object> details = new HashMap<>();
        details.put("outputSources", checkOutputSources());
        details.put("memoryUsage", getMemoryUsage());
        
        builder.up()
               .withDetails(details);
    }
    
    private Map<String, String> checkOutputSources() {
        Map<String, String> status = new HashMap<>();
        outputSourceManager.getOutputSources().forEach((type, source) -> {
            status.put(type.name(), source.isHealthy() ? "UP" : "DOWN");
        });
        return status;
    }
} 