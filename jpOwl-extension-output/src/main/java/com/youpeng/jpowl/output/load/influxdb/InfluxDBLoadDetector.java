package com.youpeng.jpowl.output.load.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;
import com.youpeng.jpowl.output.load.LoadDetector;
import com.youpeng.jpowl.output.model.LoadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InfluxDB负载检测器
 */
public class InfluxDBLoadDetector implements LoadDetector {
    private static final Logger logger = LoggerFactory.getLogger(InfluxDBLoadDetector.class);
    private final InfluxDBClient client;
    
    public InfluxDBLoadDetector(InfluxDBClient client) {
        this.client = client;
    }
    
    @Override
    public LoadStatus detect() {
        try {
            Map<String, Object> stats = getSystemStats();
            
            double cpuUsage = calculateCpuUsage(stats);
            double memoryUsage = calculateMemoryUsage(stats);
            double diskIoUsage = calculateDiskIoUsage(stats);
            double connectionUsage = calculateConnectionUsage(stats);
            
            boolean overloaded = isOverloaded(cpuUsage, memoryUsage, connectionUsage);
            
            return LoadStatus.create()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .connectionUsage(connectionUsage)
                .diskIoUsage(diskIoUsage)
                .systemLoad(0.0)
                .overloaded(overloaded)
                .build();
                
        } catch (Exception e) {
            logger.error("Failed to detect InfluxDB load", e);
            return createDefaultLoadStatus();
        }
    }
    
    private Map<String, Object> getSystemStats() {
        String query =
                "from(bucket: \"_monitoring\")\n" +
                        "  |> range(start: -1m)\n" +
                        "  |> filter(fn: (r) => r[\"_measurement\"] == \"influxdb_system\")\n" +
                        "  |> last()\n";
            
        List<FluxTable> tables = client.getQueryApi().query(query);
        Map<String, Object> stats = new HashMap<>();
        
        tables.forEach(table -> 
            table.getRecords().forEach(record -> 
                stats.put(record.getField(), record.getValue())));
                
        return stats;
    }
    
    private double calculateCpuUsage(Map<String, Object> stats) {
        return (double) stats.getOrDefault("cpu_usage", 0.0);
    }
    
    private double calculateMemoryUsage(Map<String, Object> stats) {
        return (double) stats.getOrDefault("memory_usage", 0.0);
    }
    
    private double calculateDiskIoUsage(Map<String, Object> stats) {
        return (double) stats.getOrDefault("disk_io", 0.0);
    }
    
    private double calculateConnectionUsage(Map<String, Object> stats) {
        return (double) stats.getOrDefault("http_active_requests", 0.0);
    }
    
    private boolean isOverloaded(double cpuUsage, double memoryUsage, double connectionUsage) {
        return cpuUsage > 80 || memoryUsage > 85 || connectionUsage > 90;
    }
    
    private LoadStatus createDefaultLoadStatus() {
        return LoadStatus.create()
            .cpuUsage(0)
            .memoryUsage(0)
            .connectionUsage(0)
            .diskIoUsage(0)
            .systemLoad(0)
            .overloaded(false)
            .build();
    }
} 