package com.youpeng.jpowl.output.manager;

import com.youpeng.jpowl.output.model.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 输出源缓存管理器
 */
public class OutputSourceCacheManager {
    private static final Logger logger = LoggerFactory.getLogger(OutputSourceCacheManager.class);

    private static final Map<String, LinkedBlockingQueue<MonitorData>> cacheMap = new ConcurrentHashMap<>();
    private static final int DEFAULT_CACHE_SIZE = 10000;
    
    /**
     * 缓存数据
     */
    public static boolean cache(String key, MonitorData data) {
        LinkedBlockingQueue<MonitorData> queue = cacheMap.computeIfAbsent(key, 
            k -> new LinkedBlockingQueue<>(DEFAULT_CACHE_SIZE));
            
        return queue.offer(data);
    }
    
    /**
     * 获取并清空缓存
     */
    public static List<MonitorData> drainCache(String key) {
        LinkedBlockingQueue<MonitorData> queue = cacheMap.get(key);
        if (queue == null) {
            return new ArrayList<>();
        }
        
        List<MonitorData> result = new ArrayList<>();
        queue.drainTo(result);
        return result;
    }
    
    /**
     * 获取缓存大小
     */
    public static int getCacheSize(String key) {
        LinkedBlockingQueue<MonitorData> queue = cacheMap.get(key);
        return queue != null ? queue.size() : 0;
    }
    
    /**
     * 清除缓存
     */
    public static void clearCache(String key) {
        LinkedBlockingQueue<MonitorData> queue = cacheMap.remove(key);
        if (queue != null) {
            queue.clear();
        }
    }
} 