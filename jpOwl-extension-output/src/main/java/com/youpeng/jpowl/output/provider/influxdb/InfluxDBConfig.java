package com.youpeng.jpowl.output.provider.influxdb;

import org.apache.commons.lang3.StringUtils;
import java.util.Map;

public class InfluxDBConfig {
    private String url;
    private String token;
    private String org;
    private String bucket;
    private String measurement;
    private int batchSize;
    private int flushInterval;
    
    public static InfluxDBConfig fromMap(Map<String, Object> properties) {
        InfluxDBConfig config = new InfluxDBConfig();
        
        // 必填项
        config.url = getRequiredString(properties, "url");
        config.token = getRequiredString(properties, "token");
        config.org = getRequiredString(properties, "org");
        config.bucket = getRequiredString(properties, "bucket");
        
        // 选填项（带默认值）
        config.measurement = (String) properties.getOrDefault("measurement", "jpowl_monitor");
        config.batchSize = getIntValue(properties, "batchSize", 1000);
        config.flushInterval = getIntValue(properties, "flushInterval", 1000);
        
        config.validate();
        return config;
    }

    public void validate() {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url cannot be empty");
        }
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("token cannot be empty");
        }
        if (StringUtils.isBlank(org)) {
            throw new IllegalArgumentException("org cannot be empty");
        }
        if (StringUtils.isBlank(bucket)) {
            throw new IllegalArgumentException("bucket cannot be empty");
        }
    }

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(Integer flushInterval) {
        this.flushInterval = flushInterval;
    }

    private static String getRequiredString(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null || StringUtils.isBlank(value.toString())) {
            throw new IllegalArgumentException(key + " cannot be empty");
        }
        return value.toString();
    }
    
    private static int getIntValue(Map<String, Object> properties, String key, int defaultValue) {
        Object value = properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a valid integer");
        }
    }
} 