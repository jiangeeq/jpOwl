public class MemoryOutputSource implements OutputSource {
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
    public OutputSourceType getType() {
        return OutputSourceType.MEMORY;
    }
    
    // 提供查询接口
    public List<MonitorData> getLatestData(int n) {
        return buffer.stream()
            .limit(Math.min(n, capacity))
            .collect(Collectors.toList());
    }
} 