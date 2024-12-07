package com.youpeng.jpowl.logging.decorator;

import com.youpeng.jpowl.logging.model.LogEvent;

public class PerformanceDecorator implements LogEventDecorator {
    private final Runtime runtime = Runtime.getRuntime();
    
    @Override
    public LogEvent decorate(LogEvent event) {
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
         return event.addMdc("memory", formatSize(usedMemory))
            .addMdc("totalMemory", formatSize(totalMemory))
            .addMdc("freeMemory", formatSize(freeMemory))
            .addMdc("processors", String.valueOf(runtime.availableProcessors()));
    }
    
    private String formatSize(long bytes) {
        return bytes / (1024 * 1024) + "MB";
    }
} 