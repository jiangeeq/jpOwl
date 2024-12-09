package com.youpeng.jpowl.core.spi;

import com.youpeng.jpowl.core.api.LogAdapter;
import java.util.Map;

/**
 * 日志适配器提供者接口
 */
public interface LogAdapterProvider {
    /**
     * 创建日志适配器
     */
    LogAdapter createAdapter(Map<String, Object> properties);
    
    /**
     * 获取适配器类型
     */
    String getType();
    
    /**
     * 是否支持当前环境
     */
    boolean isSupported();
} 