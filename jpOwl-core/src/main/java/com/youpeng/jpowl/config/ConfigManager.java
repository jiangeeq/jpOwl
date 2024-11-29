package com.youpeng.jpowl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ConfigManager {
    private static ConcurrentMap<String, String> configMap = new ConcurrentHashMap<>();
    @Value("${jpowl.log.level:INFO}")
    private static LogLevel logLevel;
    private static Properties properties = new Properties();

    static {
        // Load properties from file
        try {
            properties.load(ConfigManager.class.getClassLoader().getResourceAsStream("application.properties"));
            properties.forEach((key,value)-> configMap.put((String) key, (String) value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return configMap.get(key);
    }

    public static void setProperty(String key, String value) {
        configMap.put(key, value);
    }
    public static LogLevel  getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(LogLevel level) {
        logLevel = level;
    }
}
