package com.youpeng.jpowl.spring.config;

import com.youpeng.jpowl.core.OutputSource;
import com.youpeng.jpowl.spring.template.JpOwlTemplate;
import com.youpeng.jpowl.spring.factory.OutputSourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * JpOwl 自动配置类
 * 负责自动装配核心组件
 */
@Configuration
@ConditionalOnClass(JpOwlTemplate.class)
@EnableConfigurationProperties(JpOwlProperties.class)
@Import({JpOwlConfigurationImportSelector.class})
public class JpOwlAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OutputSourceFactory outputSourceFactory(ObjectProvider<OutputSource> outputSources) {
        return new OutputSourceFactory(outputSources);
    }

    @Bean
    @ConditionalOnMissingBean
    public JpOwlTemplate jpOwlTemplate(OutputSourceFactory outputSourceFactory) {
        return new JpOwlTemplate(outputSourceFactory.getOutputSource());
    }
} 