package com.youpeng.jpowl.output.provider.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.youpeng.jpowl.output.core.AbstractOutputSource;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.exception.OutputSourceException;
import com.youpeng.jpowl.output.metrics.MetricsCollector;
import com.youpeng.jpowl.output.model.MonitorData;
import com.youpeng.jpowl.output.util.RetryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * InfluxDB输出源实现
 */
public class InfluxDBOutputSource extends AbstractOutputSource {
    private static final Logger logger = LoggerFactory.getLogger(InfluxDBOutputSource.class);

    private final InfluxDBClient client;
    private final WriteApi writeApi;
    private final String measurement;
    private static final int MAX_RETRY_TIMES = 3;
    private static final long RETRY_INTERVAL_MS = 1000;
    private static final int MAX_BATCH_SIZE = 5000;
    
    public InfluxDBOutputSource(Map<String, Object> properties) {
        super(properties);
        InfluxDBConfig config = InfluxDBConfig.fromMap(properties);
        
        this.client = createClient(config);
        this.writeApi = createWriteApi(config);
        this.measurement = config.getMeasurement();
    }
    
    private InfluxDBClient createClient(InfluxDBConfig config) {
        return InfluxDBClientFactory.create(
            config.getUrl(),
            config.getToken().toCharArray(),
            config.getOrg(),
            config.getBucket()
        );
    }
    
    private WriteApi createWriteApi(InfluxDBConfig config) {
        return client.makeWriteApi(WriteOptions.builder()
            .batchSize(config.getBatchSize())
            .flushInterval(config.getFlushInterval())
            .build());
    }
    
    @Override
    public void write(MonitorData data) {
        RetryUtil.executeWithRetry(() -> {
            try {
                Point point = convertToPoint(data);
                writeApi.writePoint(point);
                MetricsCollector.recordWrite(getType(), true, 0);
            } catch (Exception e) {
                MetricsCollector.recordWrite(getType(), false, 0);
                throw new OutputSourceException(getType(), "Failed to write data", e);
            }
            return null;
        }, MAX_RETRY_TIMES, RETRY_INTERVAL_MS);
    }
    
    @Override
    public void writeBatch(List<MonitorData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        
        // 如果批量大小超过限制，分批处理
        if (dataList.size() > MAX_BATCH_SIZE) {
            for (int i = 0; i < dataList.size(); i += MAX_BATCH_SIZE) {
                int end = Math.min(i + MAX_BATCH_SIZE, dataList.size());
                writeBatch(dataList.subList(i, end));
            }
            return;
        }
        
        try {
            List<Point> points = dataList.stream()
                .map(this::convertToPoint)
                .collect(Collectors.toList());
            writeApi.writePoints(points);
            MetricsCollector.recordWrite(getType(), true, 0);
        } catch (Exception e) {
            MetricsCollector.recordWrite(getType(), false, 0);
            logger.error("Failed to write batch data to InfluxDB", e);
            throw new OutputSourceException(getType(), "Failed to write batch data", e);
        }
    }
    
    private Point convertToPoint(MonitorData data) {
        Point point = Point.measurement(measurement)
            .time(data.getTimestamp().toEpochMilli(), WritePrecision.MS)
            .addField("value", data.getValue());
            
        // 添加标签
        if (data.getTags() != null) {
            data.getTags().forEach(point::addTag);
        }
        
        // 添加其他字段
        if (data.getAttributes() != null) {
            data.getAttributes().forEach((key, value) -> {
                if (value instanceof Number) {
                    point.addField(key, (Number) value);
                } else {
                    point.addField(key, String.valueOf(value));
                }
            });
        }
        
        return point;
    }
    
    @Override
    public OutputSourceType getType() {
        return OutputSourceType.INFLUXDB;
    }
    
    @Override
    public void close() {
        try {
            writeApi.close();
            client.close();
        } catch (Exception e) {
            logger.error("Error closing InfluxDBOutputSource", e);
        }
    }
} 