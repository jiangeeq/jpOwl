package com.youpeng.jpowl.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 输出操作回调接口
 */
public interface OutputCallback {
    /**
     * 操作成功回调
     */
    void onSuccess();
    
    /**
     * 操作失败回调
     * @param e 异常信息
     */
    void onFailure(Exception e);
    
    /**
     * 创建默认回调实现
     */
    static OutputCallback defaultCallback() {
        return new OutputCallback() {
            private final Logger logger = LoggerFactory.getLogger(OutputCallback.class);
            
            @Override
            public void onSuccess() {
                logger.debug("Output operation completed successfully");
            }
            
            @Override
            public void onFailure(Exception e) {
                logger.error("Output operation failed", e);
            }
        };
    }
} 