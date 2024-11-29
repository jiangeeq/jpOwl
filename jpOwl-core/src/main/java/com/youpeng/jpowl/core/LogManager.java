package com.youpeng.jpowl.core;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.youpeng.jpowl.config.LogLevel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class LogManager {
    //为了改进性能监控，可以使用Disruptor进行高性能日志处理
    private final Disruptor<LogEvent> disruptor;

    private final ConcurrentMap<String, AtomicReference<LogLevel>> logLevels = new ConcurrentHashMap<>();

    public LogManager() {
        // 初始化日志级别
        logLevels.put("default", new AtomicReference<>(LogLevel.INFO));
        ExecutorService executor = Executors.newCachedThreadPool();
        LogEventFactory factory = new LogEventFactory();
        int bufferSize = 1024;
        disruptor = new Disruptor<>(factory, bufferSize, executor);
        disruptor.handleEventsWith(new LogEventHandler());
        disruptor.start();
    }

    public void setLogLevel(String loggerName, LogLevel level) {
        logLevels.computeIfAbsent(loggerName, k -> new AtomicReference<>(LogLevel.INFO)).set(level);
    }

    public void log(String loggerName, LogLevel level, String message) {
        // 获取 RingBuffer 对象，用于获取和发布事件
        RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();

        // 获取下一个可用的序列号
        long sequence = ringBuffer.next();
        try {
            // 通过序列号获取 LogEvent 对象
            LogEvent event = ringBuffer.get(sequence);

            // 设置日志消息
            event.set(message);
        } finally {
            // 发布事件，表示事件已经填充完毕，可以进行处理
            ringBuffer.publish(sequence);
        }
        AtomicReference<LogLevel> currentLevel = logLevels.getOrDefault(loggerName, logLevels.get("default"));
        if (level.isEnabled(currentLevel.get())) {
            // 记录日志
            System.out.println(message);
        }
    }
}

class LogEvent {
    private String message;

    public void set(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

class LogEventFactory implements EventFactory<LogEvent> {
    @Override
    public LogEvent newInstance() {
        return new LogEvent();
    }
}

class LogEventHandler implements EventHandler<LogEvent> {
    @Override
    public void onEvent(LogEvent event, long sequence, boolean endOfBatch) {
        // 处理日志事件
        System.out.println(event.getMessage());
    }
}