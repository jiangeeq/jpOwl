package com.youpeng.jpowl.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志上下文管理
 */
public class LogContext {
    private static final ThreadLocal<Map<String, String>> MDC = 
        ThreadLocal.withInitial(HashMap::new);
    
    public static void put(String key, String value) {
        MDC.get().put(key, value);
    }
    
    public static String get(String key) {
        return MDC.get().get(key);
    }
    
    public static void remove(String key) {
        MDC.get().remove(key);
    }
    
    public static void clear() {
        MDC.get().clear();
    }
    
    public static Map<String, String> getCopyOfContextMap() {
        return new HashMap<>(MDC.get());
    }
} 