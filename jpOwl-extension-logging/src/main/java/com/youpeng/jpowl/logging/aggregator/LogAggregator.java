package com.youpeng.jpowl.logging.aggregator;

import com.youpeng.jpowl.logging.model.LogEvent; /**
 * 日志聚合器接口
 * 用于对日志事件进行聚合处理
 */
public interface LogAggregator {
    /**
     * 添加日志事件
     */
    void add(LogEvent event);
    
    /**
     * 获取聚合结果
     */
    AggregationResult getResult();
    
    /**
     * 重置聚合器状态
     */
    void reset();
}
