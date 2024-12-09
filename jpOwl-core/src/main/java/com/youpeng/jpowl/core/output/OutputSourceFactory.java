package com.youpeng.jpowl.core.output;

import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.spi.OutputSourceProvider;
import com.youpeng.jpowl.core.enums.OutputSourceType;
import com.youpeng.jpowl.core.util.ServiceLoaderUtils;
import java.util.Map;

/**
 * 输出源工厂类
 * 负责创建和管理输出源实例
 * 
 * @author youpeng
 * @since 1.0.0
 */
public class OutputSourceFactory {
    /**
     * 输出源提供者映射表
     * 通过SPI机制加载所有可用的输出源提供者
     */
    private static final Map<OutputSourceType, OutputSourceProvider> PROVIDERS = 
        ServiceLoaderUtils.loadServices(
            OutputSourceProvider.class,
            OutputSourceProvider::getType
        );
    
    /**
     * 创建输出源实例
     * 
     * @param type 输出源类型
     * @param properties 配置属性
     * @return 输出源实例
     * @throws IllegalArgumentException 如果不支持指定的输出源类型
     */
    public static OutputSource createOutputSource(
            OutputSourceType type, 
            Map<String, Object> properties) {
        OutputSourceProvider provider = PROVIDERS.get(type);
        if (provider == null) {
            throw new IllegalArgumentException(
                "Unsupported output source type: " + type);
        }
        return provider.createOutputSource(properties);
    }
} 