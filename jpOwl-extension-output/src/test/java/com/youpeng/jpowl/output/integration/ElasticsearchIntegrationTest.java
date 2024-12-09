package com.youpeng.jpowl.output.integration;

import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.manager.OutputSourceManager;
import com.youpeng.jpowl.output.model.MonitorData;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

@Testcontainers
public class ElasticsearchIntegrationTest {

    @Container
    private static final ElasticsearchContainer elasticsearch = 
        new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.9.3");
    
    @Test
    void testElasticsearchOutput() {
        OutputSourceManager manager = new OutputSourceManager();
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("hosts", elasticsearch.getHttpHostAddress());
        properties.put("index", "test_metrics");
        
        manager.register(OutputSourceType.ELASTICSEARCH, properties);
        
        MonitorData data = new MonitorData();
        data.setMetricName("test_metric");
        data.setValue(100.0);
        
        manager.write(data);
        
        // TODO: 添加ES查询验证
    }
} 