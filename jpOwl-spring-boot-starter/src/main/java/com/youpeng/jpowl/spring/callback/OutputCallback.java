package com.youpeng.jpowl.spring.callback;

/**
 * 输出回调接口
 * 用于处理输出操作的结果
 */
public interface OutputCallback {

    /**
     * 输出成功的回调
     * @param data 输出的数据
     */
    void onSuccess(Object data);

    /**
     * 输出失败的回调
     * @param data 输出的数据
     * @param throwable 异常信息
     */
    void onError(Object data, Throwable throwable);
} 