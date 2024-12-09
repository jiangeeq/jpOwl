package com.youpeng.jpowl.output.provider.memory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.load.LoadDetectorFactory;
import com.youpeng.jpowl.output.model.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MemoryOutputSource implements OutputSource {
    private static final Logger logger = LoggerFactory.getLogger(MemoryOutputSource.class);

    private final RingBuffer<MonitorData> buffer;
    private final int capacity;
    
    public MemoryOutputSource(int capacity) {
        this.capacity = capacity;
        this.buffer = RingBuffer.createSingleProducer(
            new MonitorDataFactory(), capacity, new YieldingWaitStrategy());
    }
    
    @Override
    public void write(MonitorData data) {
        long sequence = buffer.next();
        try {
            MonitorData event = buffer.get(sequence);
            event.setData(data);
        } finally {
            buffer.publish(sequence);
        }
    }

    @Override
    public void writeBatch(List<MonitorData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        
        // 获取连续的序列号范围
        int batchSize = dataList.size();
        long firstSequence = buffer.next(batchSize);  // 获取第一个序列号
        long lastSequence = firstSequence + batchSize - 1;  // 计算最后一个序列号
        
        try {
            // 批量写入数据
            for (int i = 0; i < batchSize; i++) {
                MonitorData event = buffer.get(firstSequence + i);
                event.setData(dataList.get(i));
            }
        } finally {
            // 发布序列号范围
            buffer.publish(firstSequence, lastSequence);
        }
    }

    @Override
    public OutputSourceType getType() {
        return OutputSourceType.MEMORY;
    }

    @Override
    public void close() {
        try {
            // 等待所有正在进行的写入完成
            long currentSequence = buffer.getCursor();
            buffer.publish(currentSequence);
            
            // 帮助GC回收
            for (long seq = 0; seq < capacity; seq++) {
                buffer.get(seq).setData(null);
            }
        } catch (Exception e) {
            logger.error("Error closing MemoryOutputSource", e);
        }
    }

    // 提供查询接口
    public List<MonitorData> getLatestData(int n) {
        List<MonitorData> result = new ArrayList<>();
        long cursor = buffer.getCursor();
        for (int i = 0; i < Math.min(n, capacity); i++) {
            result.add(buffer.get(cursor - i));
        }
        return result;
    }
} 