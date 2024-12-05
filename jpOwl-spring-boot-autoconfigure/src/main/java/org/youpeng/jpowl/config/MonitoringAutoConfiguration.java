package org.youpeng.jpowl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.youpeng.jpowl.intercept.MonitoringInterceptor;

@Configuration
public class MonitoringAutoConfiguration {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(MonitoringInterceptor monitoringInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(monitoringInterceptor).addPathPatterns("/**");
            }
        };
    }

    @Bean
    public MonitoringInterceptor monitoringInterceptor() {
        return new MonitoringInterceptor();
    }
}
