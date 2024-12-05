public class AsyncFileOutputSource implements OutputSource {
    private final AsyncFileChannel channel;
    private final ObjectMapper objectMapper;
    private final BlockingQueue<MonitorData> queue;
    
    public AsyncFileOutputSource(String filePath) {
        this.channel = AsyncFileChannel.open(Paths.get(filePath));
        this.objectMapper = new ObjectMapper();
        this.queue = new LinkedBlockingQueue<>(10000);
        startWriteThread();
    }
    
    private void startWriteThread() {
        Thread writeThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    MonitorData data = queue.take();
                    String json = objectMapper.writeValueAsString(data) + "\n";
                    ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());
                    channel.write(buffer, channel.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        writeThread.setDaemon(true);
        writeThread.start();
    }
    
    @Override
    public void write(MonitorData data) {
        if (!queue.offer(data)) {
            // 处理队列满的情况
            log.warn("Output queue is full, dropping data");
        }
    }
    
    @Override
    public OutputSourceType getType() {
        return OutputSourceType.FILE;
    }
} 