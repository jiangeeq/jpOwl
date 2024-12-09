package com.youpeng.jpowl.output.load.mysql;

import com.youpeng.jpowl.output.load.LoadDetector;
import com.youpeng.jpowl.output.model.LoadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * MySQL负载检测器
 */
public class MySQLLoadDetector implements LoadDetector {
    private static final Logger logger = LoggerFactory.getLogger(MySQLLoadDetector.class);
    private final DataSource dataSource;
    
    public MySQLLoadDetector(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public LoadStatus detect() {
        try {
            Map<String, Object> stats = getMySQLStats();
            
            double cpuUsage = calculateCpuUsage(stats);
            double memoryUsage = calculateMemoryUsage(stats);
            double connectionUsage = calculateConnectionUsage(stats);
            double diskIoUsage = calculateDiskIoUsage(stats);
            double systemLoad = calculateSystemLoad(stats);
            
            boolean overloaded = isOverloaded(stats);
            
            return LoadStatus.create()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .connectionUsage(connectionUsage)
                .diskIoUsage(diskIoUsage)
                .systemLoad(systemLoad)
                .overloaded(overloaded)
                .build();
                
        } catch (Exception e) {
            logger.error("Failed to detect MySQL load", e);
            return LoadStatus.create().build();
        }
    }
    
    private Map<String, Object> getMySQLStats() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 获取全局状态信息
            try (ResultSet rs = stmt.executeQuery("SHOW GLOBAL STATUS")) {
                while (rs.next()) {
                    stats.put(rs.getString(1).toLowerCase(), rs.getString(2));
                }
            }
            
            // 获取全局变量
            try (ResultSet rs = stmt.executeQuery("SHOW GLOBAL VARIABLES")) {
                while (rs.next()) {
                    stats.put("var_" + rs.getString(1).toLowerCase(), rs.getString(2));
                }
            }
            
            // 获取进程列表统计
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN command != 'Sleep' THEN 1 ELSE 0 END) as active " +
                "FROM information_schema.processlist")) {
                if (rs.next()) {
                    stats.put("total_connections", rs.getInt("total"));
                    stats.put("active_connections", rs.getInt("active"));
                }
            }
        }
        
        return stats;
    }
    
    private double calculateCpuUsage(Map<String, Object> stats) {
        // 通过活跃连接数和查询数估算CPU使用率
        int activeConnections = parseInt(stats.get("active_connections"));
        long queries = parseLong(stats.get("questions"));
        return Math.min(100.0, (activeConnections * 10 + queries / 1000.0));
    }
    
    private double calculateMemoryUsage(Map<String, Object> stats) {
        long globalBufferSize = parseLong(stats.get("var_innodb_buffer_pool_size"));
        long usedBufferPages = parseLong(stats.get("innodb_buffer_pool_pages_total"))
            - parseLong(stats.get("innodb_buffer_pool_pages_free"));
        
        if (globalBufferSize <= 0) {
            return 0.0;
        }
        
        return (double) usedBufferPages / globalBufferSize * 100;
    }
    
    private double calculateConnectionUsage(Map<String, Object> stats) {
        int maxConnections = parseInt(stats.get("var_max_connections"));
        int currentConnections = parseInt(stats.get("total_connections"));
        
        if (maxConnections <= 0) {
            return 0.0;
        }
        
        return (double) currentConnections / maxConnections * 100;
    }
    
    private double calculateDiskIoUsage(Map<String, Object> stats) {
        long innodbDataReads = parseLong(stats.get("innodb_data_reads"));
        long innodbDataWrites = parseLong(stats.get("innodb_data_writes"));
        
        // 计算IOPS
        return Math.min(100.0, (innodbDataReads + innodbDataWrites) / 1000.0);
    }
    
    private double calculateSystemLoad(Map<String, Object> stats) {
        int threads_running = parseInt(stats.get("threads_running"));
        int threads_connected = parseInt(stats.get("threads_connected"));
        
        return Math.min(100.0, (double) threads_running / Math.max(1, threads_connected) * 100);
    }
    
    private boolean isOverloaded(Map<String, Object> stats) {
        return calculateConnectionUsage(stats) > 80
            || calculateSystemLoad(stats) > 70
            || parseInt(stats.get("threads_running")) > 30
            || parseLong(stats.get("slow_queries")) > 100;
    }
    
    private int parseInt(Object value) {
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }
    
    private long parseLong(Object value) {
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (Exception e) {
            return 0L;
        }
    }
} 