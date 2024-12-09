package com.youpeng.jpowl.output.load;


import com.youpeng.jpowl.output.model.LoadStatus;

/**
 * 负载检测器接口
 */
public interface LoadDetector {
    /**
     * 获取当前负载状态
     */
    LoadStatus detect();
    

} 