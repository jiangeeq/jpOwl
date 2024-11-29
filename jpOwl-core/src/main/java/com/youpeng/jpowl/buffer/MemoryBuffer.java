package com.youpeng.jpowl.buffer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 基于内存级别的数据缓冲
 */
public class MemoryBuffer {
    protected final BlockingQueue<String> queue;
    protected final int maxSize;

    public MemoryBuffer(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedBlockingQueue<>(maxSize);
    }

    public void add(String message) {
        if (queue.size() >= maxSize * 0.8) {
            // Drop low priority logs
            queue.poll();
        }
        queue.offer(message);
    }

    public String take() throws InterruptedException {
        return queue.take();
    }
}
