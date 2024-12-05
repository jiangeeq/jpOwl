package com.youpeng.jpowl.spring;

import org.springframework.stereotype.Component;

@Component
public class JpOwlTemplate {
    private final JpOwlCore core;
    private final AlertExtension alertExtension;
    
    public JpOwlTemplate(JpOwlProperties properties) {
        // 构建核心配置
        JpOwlCoreConfig coreConfig = new JpOwlCoreConfig.Builder()
            .outputType(properties.getOutputType())
            .filePath(properties.getFilePath())
            .bufferSize(properties.getBufferSize())
            .build();
            
        // 初始化核心组件
        this.core = new JpOwlCore(coreConfig);
        
        // 根据配置决定是否启用告警扩展
        if (properties.isAlertEnabled()) {
            this.alertExtension = new AlertExtension(properties.getAlertConfig());
        } else {
            this.alertExtension = null;
        }
    }
    
    public void logTransaction(String name, long duration) {
        core.logTransaction(name, duration);
        if (alertExtension != null) {
            alertExtension.handleAlert(new Transaction(name, duration));
        }
    }
} 