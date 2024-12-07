package com.youpeng.jpowl.logging.model;

import java.util.Map;
import java.util.HashMap;
/**
 * 日志上下文管理器
 * 用于管理日志相关的上下文信息
 */
public class LogContext {
    private static Object HashMap;
    private static final ThreadLocal<Map<String, String>> MDC = ThreadLocal.withInitial(HashMap::new);
    
    /**
     * 设置上下文属性
     */
    public static void put(String key, String value) {
        MDC.get().put(key, value);
    }
    
    /**
     * 获取上下文属性
     */
    public static String get(String key) {
        return MDC.get().get(key);
    }
    
    /**
     * 移除上下文属性
     */
    public static void remove(String key) {
        MDC.get().remove(key);
    }
    
    /**
     * 清空当前线程的上下文
     */
    public static void clear() {
        MDC.get().clear();
    }
    
    /**
     * 获取当前上下文的副本
     */
    public static Map<String, String> getCopyOfContextMap() {
        return new HashMap<>(MDC.get());
    }
    
    /**
     * 设置完整的上下文映射
     */
    public static void setContextMap(Map<String, String> contextMap) {
        MDC.get().clear();
        if (contextMap != null) {
            MDC.get().putAll(contextMap);
        }
    }
} 