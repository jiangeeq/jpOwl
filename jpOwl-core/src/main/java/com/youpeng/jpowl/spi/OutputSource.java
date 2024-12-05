public interface OutputSource extends Extension {
    /**
     * 初始化输出源
     */
    void initialize(Map<String, Object> properties) throws Exception;
    
    /**
     * 写入监控数据
     */
    void write(MonitorData data) throws Exception;
    
    /**
     * 批量写入监控数据
     */
    void writeBatch(List<MonitorData> dataList) throws Exception;
    
    /**
     * 刷新缓冲区
     */
    void flush() throws Exception;
    
    /**
     * 关闭输出源
     */
    void close() throws Exception;
    
    /**
     * 获取输出源类型
     */
    OutputSourceType getType();
}

public enum OutputSourceType {
    MEMORY, FILE, MONGODB, ELASTICSEARCH
} 