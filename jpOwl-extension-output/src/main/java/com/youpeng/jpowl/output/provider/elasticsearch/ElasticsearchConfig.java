package com.youpeng.jpowl.output.provider.elasticsearch;

import com.youpeng.jpowl.output.core.config.OutputSourceConfig;
import com.youpeng.jpowl.output.core.OutputSourceType;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

/**
 * Elasticsearch配置属性
 */
public class ElasticsearchConfig extends OutputSourceConfig {
    private String hosts = "localhost:9200";
    private String username;
    private String password;
    private String indexName = "jpowl_monitor";
    private Integer bulkActions = 1000;
    private Integer bulkSizeMb = 5;
    private Integer flushInterval = 5;
    private Integer concurrentRequests = 1;

    public ElasticsearchConfig() {
        super(OutputSourceType.ELASTICSEARCH);
    }

    @Override
    public void validate() {
        if (StringUtils.isBlank(hosts)) {
            throw new IllegalArgumentException("hosts cannot be empty");
        }
        if (StringUtils.isBlank(indexName)) {
            throw new IllegalArgumentException("indexName cannot be empty");
        }
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public Integer getBulkActions() {
        return bulkActions;
    }

    public void setBulkActions(Integer bulkActions) {
        this.bulkActions = bulkActions;
    }

    public Integer getBulkSizeMb() {
        return bulkSizeMb;
    }

    public void setBulkSizeMb(Integer bulkSizeMb) {
        this.bulkSizeMb = bulkSizeMb;
    }

    public Integer getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(Integer flushInterval) {
        this.flushInterval = flushInterval;
    }

    public Integer getConcurrentRequests() {
        return concurrentRequests;
    }

    public void setConcurrentRequests(Integer concurrentRequests) {
        this.concurrentRequests = concurrentRequests;
    }

    public static ElasticsearchConfig fromMap(Map<String, Object> properties) {
        ElasticsearchConfig config = new ElasticsearchConfig();
        
        // 必填项
        config.hosts = getRequiredString(properties, "hosts");
        config.indexName = getRequiredString(properties, "indexName");
        
        // 选填项
        config.username = (String) properties.getOrDefault("username", null);
        config.password = (String) properties.getOrDefault("password", null);
        config.bulkActions = getIntValue(properties, "bulkActions", 1000);
        config.bulkSizeMb = getIntValue(properties, "bulkSizeMb", 5);
        config.flushInterval = getIntValue(properties, "flushInterval", 5);
        config.concurrentRequests = getIntValue(properties, "concurrentRequests", 1);
        
        return config;
    }

    // 添加与 InfluxDBConfig 相同的工具方法
    private static String getRequiredString(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null || StringUtils.isBlank(value.toString())) {
            throw new IllegalArgumentException(key + " cannot be empty");
        }
        return value.toString();
    }
    
    private static int getIntValue(Map<String, Object> properties, String key, int defaultValue) {
        Object value = properties.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a valid integer");
        }
    }
}