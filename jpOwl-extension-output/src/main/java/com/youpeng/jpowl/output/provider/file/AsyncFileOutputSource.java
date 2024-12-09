package com.youpeng.jpowl.output.provider.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.model.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncFileOutputSource implements OutputSource {
    private static final Logger logger = LoggerFactory.getLogger(AsyncFileOutputSource.class);
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
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
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
            logger.warn("Output queue is full, dropping data");
        }
    }

    @Override
    public void writeBatch(List<MonitorData> dataList) {

    }

    @Override
    public OutputSourceType getType() {
        return OutputSourceType.FILE;
    }

    @Override
    public void close() {

    }
} 