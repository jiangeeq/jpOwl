package com.youpeng.jpowl.spring.config;

import com.youpeng.jpowl.core.JpOwlCore;
import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.load.LoadManager;
import com.youpeng.jpowl.spring.actuator.JpOwlMetrics;
import com.youpeng.jpowl.spring.aop.MonitorAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * JpOwl 自动配置类
 */
@Configuration
@EnableAspectJAutoProxy
public class JpOwlConfiguration {

    @Bean
    public JpOwlCore jpOwlCore(OutputSource outputSource, LoadManager loadManager) {
        return new JpOwlCore(outputSource, loadManager);
    }

    @Bean
    public MonitorAspect monitorAspect(JpOwlCore jpOwlCore) {
        return new MonitorAspect(jpOwlCore);
    }

    @Bean
    @ConditionalOnProperty(name = "jpowl.actuator.enabled", havingValue = "true", matchIfMissing = true)
    public JpOwlMetrics jpOwlMetrics(JpOwlCore jpOwlCore) {
        return new JpOwlMetrics(jpOwlCore);
    }
} 