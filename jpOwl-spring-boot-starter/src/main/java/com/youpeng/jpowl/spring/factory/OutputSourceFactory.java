package com.youpeng.jpowl.spring.factory;

import com.youpeng.jpowl.core.OutputSource;
import com.youpeng.jpowl.spring.exception.JpOwlException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.lang.NonNull;

/**
 * 输出源工厂类
 * 用于创建和管理不同类型的输出源
 */
public class OutputSourceFactory {
    
    private final ObjectProvider<OutputSource> outputSources;
    
    public OutputSourceFactory(@NonNull ObjectProvider<OutputSource> outputSources) {
        this.outputSources = outputSources;
    }
    
    /**
     * 获取配置的输出源
     * @return 输出源实例
     * @throws JpOwlException 当没有找到可用的输出源时抛出异常
     */
    public OutputSource getOutputSource() {
        OutputSource outputSource = outputSources.getIfAvailable();
        if (outputSource == null) {
            throw new JpOwlException("No OutputSource available");
        }
        return outputSource;
    }
} 