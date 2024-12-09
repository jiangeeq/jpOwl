package com.youpeng.jpowl.spring.template;

import com.youpeng.jpowl.core.OutputSource;
import com.youpeng.jpowl.spring.callback.OutputCallback;
import com.youpeng.jpowl.spring.support.JpOwlOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * JpOwl 操作模板类
 * 提供统一的数据输出操作接口
 */
public class JpOwlTemplate implements JpOwlOperations {
    
    private final OutputSource outputSource;
    
    public JpOwlTemplate(@NonNull OutputSource outputSource) {
        this.outputSource = outputSource;
    }
    
    @Override
    public void output(Object data) {
        output(data, null);
    }
    
    /**
     * 输出数据到配置的存储源，支持回调处理
     * @param data 待输出的数据
     * @param callback 输出回调处理器
     */
    public void output(Object data, @Nullable OutputCallback callback) {
        try {
            outputSource.output(data);
            if (callback != null) {
                callback.onSuccess(data);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(data, e);
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 获取当前使用的输出源
     */
    public OutputSource getOutputSource() {
        return outputSource;
    }
} 