package com.youpeng.jpowl.output.util;

import com.youpeng.jpowl.output.exception.OutputException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 配置验证工具类
 */
public class ConfigValidator {
    
    /**
     * 验证必需的字符串属性
     */
    public static void validateRequiredString(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value == null || StringUtils.isBlank(value.toString())) {
            throw new OutputException("Missing required configuration: " + key);
        }
    }
    
    /**
     * 验证必需的数值属性
     */
    public static void validateRequiredNumber(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value == null || !(value instanceof Number)) {
            throw new OutputException("Missing or invalid number configuration: " + key);
        }
    }
    
    /**
     * 验证数值范围
     */
    public static void validateNumberRange(Map<String, Object> config, String key, Number min, Number max) {
        Object value = config.get(key);
        if (value instanceof Number) {
            double numValue = ((Number) value).doubleValue();
            if (numValue < min.doubleValue() || numValue > max.doubleValue()) {
                throw new OutputException(String.format(
                    "Configuration %s must be between %s and %s", key, min, max));
            }
        }
    }
    
    /**
     * 验证正数
     */
    public static void validatePositive(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value instanceof Number && ((Number) value).doubleValue() <= 0) {
            throw new OutputException("Configuration " + key + " must be positive");
        }
    }
} 