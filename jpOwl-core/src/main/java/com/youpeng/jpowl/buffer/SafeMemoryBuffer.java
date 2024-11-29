package com.youpeng.jpowl.buffer;

/**
 * 可指定内存队列安全机制
 */
public class SafeMemoryBuffer extends MemoryBuffer {
    public SafeMemoryBuffer(int maxSize) {
        super(maxSize);
    }

    @Override
    public void add(String message) {
        if (queue.size() >= maxSize * 0.8) {
            // Drop low priority logs
            queue.removeIf(msg -> msg.contains("TRACE") || msg.contains("DEBUG") || msg.contains("INFO"));
        }
        queue.offer(message);
    }
}
