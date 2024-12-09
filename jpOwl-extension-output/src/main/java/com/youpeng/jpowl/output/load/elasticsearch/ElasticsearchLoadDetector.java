package com.youpeng.jpowl.output.load.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.nodes.NodesStatsResponse;
import com.youpeng.jpowl.output.load.LoadDetector;
import com.youpeng.jpowl.output.model.LoadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Elasticsearch负载检测器
 */
public class ElasticsearchLoadDetector implements LoadDetector {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchLoadDetector.class);
    private final ElasticsearchClient client;
    
    public ElasticsearchLoadDetector(ElasticsearchClient client) {
        this.client = client;
    }
    
    @Override
    public LoadStatus detect() {
        try {
            Map<String, Object> stats = getClusterStats();
            
            double cpuUsage = calculateCpuUsage(stats);
            double memoryUsage = calculateMemoryUsage(stats);
            double diskIoUsage = calculateDiskIoUsage(stats);
            double connectionUsage = calculateConnectionUsage(stats);
            double systemLoad = getSystemLoad(stats);
            
            boolean overloaded = isOverloaded(cpuUsage, memoryUsage, connectionUsage);
            
            return LoadStatus.create()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .connectionUsage(connectionUsage)
                .diskIoUsage(diskIoUsage)
                .systemLoad(systemLoad)
                .overloaded(overloaded)
                .build();
                
        } catch (Exception e) {
            logger.error("Failed to detect Elasticsearch load", e);
            return createDefaultLoadStatus();
        }
    }
    
    private Map<String, Object> getClusterStats() throws Exception {
        NodesStatsResponse nodesStats = client.nodes().stats();
        
        Map<String, Object> stats = new HashMap<>();
        nodesStats.nodes().values().forEach(node -> {
            stats.put("cpu_percent", node.os().cpu().percent());
            stats.put("memory_used_percent", node.os().mem().usedPercent());
            stats.put("disk_io_op", node.fs().ioStats().total().operations());
            stats.put("load_average", node.os().cpu().loadAverage().get("1m"));
        });
        
        return stats;
    }
    
    private double calculateCpuUsage(Map<String, Object> stats) {
        return (double) stats.getOrDefault("cpu_percent", 0.0);
    }
    
    private double calculateMemoryUsage(Map<String, Object> stats) {
        return (double) stats.getOrDefault("memory_used_percent", 0.0);
    }
    
    private double calculateDiskIoUsage(Map<String, Object> stats) {
        long ioOps = (long) stats.getOrDefault("disk_io_op", 0L);
        // 根据IO操作数计算使用率
        return Math.min(100.0, ioOps / 1000.0 * 100);
    }
    
    private double calculateConnectionUsage(Map<String, Object> stats) {
        // 可以通过http_stats获取连接信息
        return 0.0; // 需要具体实现
    }
    
    private double getSystemLoad(Map<String, Object> stats) {
        double[] loadAverage = (double[]) stats.get("load_average");
        return loadAverage != null ? loadAverage[0] : 0.0;
    }
    
    private boolean isOverloaded(double cpuUsage, double memoryUsage, double connectionUsage) {
        // 当任一指标超过阈值时判定为过载
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