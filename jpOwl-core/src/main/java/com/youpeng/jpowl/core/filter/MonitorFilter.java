package com.youpeng.jpowl.core.filter;

import com.youpeng.jpowl.core.model.MonitorEvent;

/**
 * 监控过滤器接口
 * 用于过滤不需要处理的监控事件
 * 
 * @author youpeng
 * @since 1.0.0
 */
public interface MonitorFilter {
    /**
     * 判断是否接受该监控事件
     *
     * @param event 监控事件
     * @return true如果接受该事件，false如果拒绝
     */
    boolean accept(MonitorEvent event);
} 