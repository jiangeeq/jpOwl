package com.youpeng.jpowl.core.spi;

import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.enums.OutputSourceType;
import java.util.Map;

/**
 * 输出源提供者接口
 */
public interface OutputSourceProvider {
    /**
     * 创建输出源
     */
    OutputSource createOutputSource(Map<String, Object> properties);
    
    /**
     * 获取支持的输出源类型
     */
    OutputSourceType getType();
} 