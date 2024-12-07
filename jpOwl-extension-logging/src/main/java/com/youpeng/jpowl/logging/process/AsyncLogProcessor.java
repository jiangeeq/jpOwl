package com.youpeng.jpowl.logging.process;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.youpeng.jpowl.logging.model.LogEvent;

/**
 * 异步日志处理器
 * 使用Disruptor实现高性能异步日志处理
 */
public class AsyncLogProcessor {
    private final RingBuffer<LogEvent> ringBuffer;
    private final LogProcessingChain processingChain;
    private static final int BUFFER_SIZE = 1024 * 8; // Must be power of 2
    private final Disruptor<LogEvent> disruptor;

    public AsyncLogProcessor(LogProcessingChain processingChain) {
        this.processingChain = processingChain;
        
        // 创建一个Disruptor实例，用于高效处理日志事件
        this.disruptor = new Disruptor<>(
            LogEvent::new, // 日志事件的构造函数
            BUFFER_SIZE, // 缓冲区大小，决定了Disruptor环形缓冲区的容量
            DaemonThreadFactory.INSTANCE, // 线程工厂，用于创建守护线程
            ProducerType.MULTI, // 生产者类型，支持多生产者写入
            new BlockingWaitStrategy() // 等待策略，定义消费者在处理事件时如何等待新事件的到来
        );

        // 设置事件处理器，当有新日志事件时调用此方法处理
        disruptor.handleEventsWith(this::handleEvent);

        // 启动Disruptor，返回环形缓冲区，这是生产者向消费者发送事件的地方
        this.ringBuffer = disruptor.start();
    }
    
    /**
     * 异步处理日志事件
     */
    public void process(LogEvent event) {
        // 获取下一个可用的序列号
        long sequence = ringBuffer.next();
        try {
            // 获取当前序列号对应的日志事件，这个对象是预先分配好的，避免了频繁创建对象
            LogEvent bufferedEvent = ringBuffer.get(sequence);
            // 将传入的日志事件复制到缓冲区中的事件
            bufferedEvent.copyFrom(event);
        } finally {
            // 发布事件，通知消费者有新事件可用
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 处理日志事件
     * @param event 要处理的事件对象
     * @param sequence 事件在 RingBuffer 中的序列号
     * @param endOfBatch 标识是否是批处理中的最后一个事件
     */
    private void handleEvent(LogEvent event, long sequence, boolean endOfBatch) {
        try {
            processingChain.process(event);
            // 如果是批处理的最后一个事件，可以进行额外操作
            if (endOfBatch) {
                // 例如：刷新缓冲区,记录日志
//                processingChain.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭日志处理器
     */
    public void shutdown() {
        // 优雅关闭 会等待所有已提交的事件处理完成后再关闭 Disruptor。
        disruptor.shutdown();
    }
}