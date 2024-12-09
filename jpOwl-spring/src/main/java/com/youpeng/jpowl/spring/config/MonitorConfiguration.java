package com.youpeng.jpowl.spring.config;

import com.youpeng.jpowl.core.monitor.MonitorManager;
import com.youpeng.jpowl.core.config.MonitorConfig;
import com.youpeng.jpowl.spring.aop.MonitorAspect;
import com.youpeng.jpowl.spring.support.MonitorBeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

/**
 * JpOwl监控配置类
 * 配置Spring环境下的监控组件
 * 
 * @author youpeng
 * @since 1.0.0
 */
@Configuration
@EnableAspectJAutoProxy
public class MonitorConfiguration {
    
    /**
     * 配置监控管理器
     */
    @Bean
    public MonitorManager monitorManager(MonitorConfig config) {
        return new MonitorManager(config);
    }
    
    /**
     * 配置监控切面
     */
    @Bean
    public MonitorAspect monitorAspect(MonitorManager monitorManager) {
        return new MonitorAspect(monitorManager);
    }
    
    /**
     * 配置监控配置
     */
    @Bean
    public MonitorConfig monitorConfig(Environment env) {
        return MonitorConfig.builder()
            .enabled(env.getProperty("jpowl.enabled", Boolean.class, true))
            .samplingRate(env.getProperty("jpowl.sampling-rate", Integer.class, 100))
            .bufferSize(env.getProperty("jpowl.buffer-size", Integer.class, 1024))
            .outputType(env.getProperty("jpowl.output.type", "FILE"))
            .outputPath(env.getProperty("jpowl.output.path", "logs/monitor.log"))
            .build();
    }
    
    /**
     * 配置Bean工厂后处理器
     */
    @Bean
    public static BeanFactoryPostProcessor monitorBeanFactoryPostProcessor() {
        return new MonitorBeanFactoryPostProcessor();
    }
} 