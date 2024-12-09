package com.youpeng.jpowl.core.handler;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.exception.MonitorException;

/**
 * 监控事件处理器接口
 * 定义了事件处理的基本行为
 * 
 * @author youpeng
 * @since 1.0.0
 */
public interface MonitorEventHandler {
    /**
     * 处理监控事件
     *
     * @param event 监控事件
     * @throws MonitorException 处理失败时抛出
     */
    void handle(MonitorEvent event) throws MonitorException;
    
    /**
     * 获取处理器名称
     *
     * @return 处理器名称
     */
    String getName();
    
    /**
     * 是否支持该事件
     *
     * @param event 监控事件
     * @return true如果支持处理该事件
     */
    boolean supports(MonitorEvent event);
    
    /**
     * 获取处理器优先级
     * 数值越小优先级越高
     *
     * @return 优先级值
     */
    default int getOrder() {
        return 0;
    }
} 