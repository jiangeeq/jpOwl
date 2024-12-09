package com.youpeng.jpowl.core.util;

import com.youpeng.jpowl.core.model.MonitorData;
import com.youpeng.jpowl.core.model.MonitorModel;
import com.youpeng.jpowl.core.enums.AlertLevel;

/**
 * 监控工具类
 */
public final class MonitorUtils {
    private MonitorUtils() {} // 防止实例化

    public static MonitorModel createAlertModel(MonitorData data, AlertLevel level) {
        if (data == null) {
            throw new IllegalArgumentException("MonitorData cannot be null");
        }
        
        String title = generateTitle(data);
        String content = generateContent(data);
        
        return new MonitorModel(
            data.getName(),
            level,
            title,
            content,
            data.getType(),
            data
        );
    }
    
    private static String generateTitle(MonitorData data) {
        return String.format("[%s] %s Alert", data.getType(), data.getName());
    }
    
    private static String generateContent(MonitorData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metrics:\n");
        data.getMetrics().forEach((k, v) -> 
            sb.append(String.format("- %s: %s\n", k, v)));
        sb.append("\nTags:\n");
        data.getTags().forEach((k, v) -> 
            sb.append(String.format("- %s: %s\n", k, v)));
        return sb.toString();
    }
} 