package com.youpeng.jpowl.core.util;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * SPI服务加载工具类
 * 提供统一的服务加载和管理功能
 * 
 * @author youpeng
 * @since 1.0.0
 */
public final class ServiceLoaderUtils {
    private ServiceLoaderUtils() {}
    
    /**
     * 加载SPI服务并按类型分类
     * 
     * @param serviceClass 服务接口类
     * @param keyMapper 服务实例到键的映射函数
     * @return 服务实例映射表
     * @param <T> 服务类型
     * @param <K> 键类型
     */
    public static <T, K> Map<K, T> loadServices(
            Class<T> serviceClass, 
            Function<T, K> keyMapper) {
        Map<K, T> providers = new ConcurrentHashMap<>();
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
        for (T provider : loader) {
            providers.put(keyMapper.apply(provider), provider);
        }
        return providers;
    }
    
    /**
     * 检查SPI服务是否可用
     * 
     * @param serviceClass 服务接口类
     * @return true如果存在可用的服务实现
     */
    public static boolean isServiceAvailable(Class<?> serviceClass) {
        ServiceLoader<?> loader = ServiceLoader.load(serviceClass);
        return loader.iterator().hasNext();
    }
} 