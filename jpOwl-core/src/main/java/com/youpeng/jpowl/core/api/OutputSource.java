package com.youpeng.jpowl.core.api;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.enums.OutputSourceType;
import java.util.List;

/**
 * 输出源接口
 * 定义了监控数据的输出行为，支持单条和批量写入
 * 
 * @author youpeng
 * @since 1.0.0
 */
public interface OutputSource {
    /**
     * 写入单条监控事件
     * 
     * @param event 监控事件
     * @throws IllegalStateException 如果输出源已关闭
     */
    void write(MonitorEvent event);
    
    /**
     * 获取输出源类型
     * 
     * @return 输出源类型枚举值
     */
    OutputSourceType getType();
    
    /**
     * 关闭输出源，释放相关资源
     */
    void close();
    
    /**
     * 是否支持批量写入
     * 默认不支持批量写入
     * 
     * @return true如果支持批量写入，否则返回false
     */
    default boolean supportsBatch() {
        return false;
    }
    
    /**
     * 批量写入监控事件
     * 默认实现是逐条写入
     * 
     * @param events 监控事件列表
     */
    default void writeBatch(List<MonitorEvent> events) {
        for (MonitorEvent event : events) {
            write(event);
        }
    }
} 