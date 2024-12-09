package com.youpeng.jpowl.output.load;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.influxdb.client.InfluxDBClient;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.exception.OutputSourceException;
import com.youpeng.jpowl.output.load.elasticsearch.ElasticsearchLoadDetector;
import com.youpeng.jpowl.output.load.influxdb.InfluxDBLoadDetector;
import com.youpeng.jpowl.output.load.mysql.MySQLLoadDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载检测器工厂
 */
public class LoadDetectorFactory {
    private static final Logger logger = LoggerFactory.getLogger(LoadDetectorFactory.class);

    private static final Map<OutputSourceType, LoadDetector> detectorMap = new ConcurrentHashMap<>();
    
    /**
     * 创建并注册负载检测器
     */
    public static void registerDetector(OutputSourceType type, Object client) {
        try {
            LoadDetector detector = createDetector(type, client);
            detectorMap.put(type, detector);
            logger.info("Registered load detector for {}", type);
        } catch (Exception e) {
            throw new OutputSourceException(type, "Failed to create load detector", e);
        }
    }
    
    /**
     * 获取负载检测器
     */
    public static LoadDetector getDetector(OutputSourceType type) {
        LoadDetector detector = detectorMap.get(type);
        if (detector == null) {
            throw new OutputSourceException(type, "No load detector found");
        }
        return detector;
    }
    
    private static LoadDetector createDetector(OutputSourceType type, Object client) {
        switch (type) {
            case ELASTICSEARCH:
                if (!(client instanceof ElasticsearchClient)) {
                    throw new IllegalArgumentException("Invalid client type for Elasticsearch");
                }
                return new ElasticsearchLoadDetector((ElasticsearchClient) client);
                
            case INFLUXDB:
                if (!(client instanceof InfluxDBClient)) {
                    throw new IllegalArgumentException("Invalid client type for Redis");
                }
                return new InfluxDBLoadDetector((InfluxDBClient) client);
                
            case MYSQL:
                if (!(client instanceof DataSource)) {
                    throw new IllegalArgumentException("Invalid client type for MySQL");
                }
                return new MySQLLoadDetector((DataSource) client);
                
            default:
                throw new IllegalArgumentException("Unsupported output source type: " + type);
        }
    }
} 