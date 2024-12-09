package com.youpeng.jpowl.output.core;

import com.youpeng.jpowl.output.model.MonitorData;

import java.util.List;

/**
 * 输出源接口
 * 定义输出源的基本行为
 */
public interface OutputSource extends AutoCloseable {
    /**
     * 写入单条数据
     */
    void write(MonitorData data);
    
    /**
     * 批量写入数据
     */
    void writeBatch(List<MonitorData> dataList);
    
    /**
     * 获取输出源类型
     */
    OutputSourceType getType();
    
    /**
     * 关闭资源
     */
    @Override
    void close();
}