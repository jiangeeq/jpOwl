package com.youpeng.jpowl.core.handler;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.exception.MonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监控事件处理链
 */
public class MonitorEventHandlerChain {
    private static final Logger logger = LoggerFactory.getLogger(MonitorEventHandlerChain.class);
    
    private final List<MonitorEventHandler> handlers = new CopyOnWriteArrayList<>();
    
    public void addHandler(MonitorEventHandler handler) {
        handlers.add(handler);
    }
    
    public void removeHandler(MonitorEventHandler handler) {
        handlers.remove(handler);
    }
    
    public void handle(MonitorEvent event) {
        List<MonitorException> exceptions = new ArrayList<>();
        
        for (MonitorEventHandler handler : handlers) {
            if (handler.supports(event)) {
                try {
                    handler.handle(event);
                } catch (MonitorException e) {
                    logger.error("Handler {} failed to process event", handler.getName(), e);
                    exceptions.add(e);
                }
            }
        }
        
        if (!exceptions.isEmpty()) {
            throw new MonitorException("Event processing failed with " + exceptions.size() + " errors", 
                exceptions.get(0));
        }
    }
} 