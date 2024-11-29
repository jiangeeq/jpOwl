package com.youpeng.jpowl.io;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

// 实现基于NIO管道的异步日志记录和数据采集，可以使用Java NIO（Non-blocking I/O）来实现高效的日志写入
// 异步数据采集可以通过Java的并发包中的ExecutorService来实现。
public class NioLogger {
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Path path;
    private FileChannel fileChannel;

    public NioLogger(String filePath) {
        if(StringUtil.isNullOrEmpty(filePath)){
//            filePath = System.getProperty("user.dir");
            filePath = "E:\\代码\\jp复习代码\\jpOwl\\src\\main\\resources\\logs\\jpowl.log";
        }
        this.path = Paths.get(filePath);
        try {
            fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {
            logQueue.offer(message);
    }

    private void startLogConsumer() {
        executorService.submit(() -> {
            while (true) {
                try {
                    String log = logQueue.take();
                    ByteBuffer buffer = ByteBuffer.wrap(log.getBytes());
                    while (buffer.hasRemaining()) {
                        fileChannel.write(buffer);
                    }
                } catch (InterruptedException | IOException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
