package com.youpeng.jpowl.logging.monitor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 滑动时间窗口计数器
 */
/**
 * 滑动时间窗口计数器类，用于在指定时间窗口内统计事件的发生次数
 * 该类使用并发队列存储事件时间戳，以实现线程安全
 */
public class SlidingTimeWindowCounter {
    // 时间窗口的持续时间，单位为毫秒
    private final long windowMillis;
    // 存储事件时间戳的队列
    private final Queue<Long> timestamps = new ConcurrentLinkedQueue<>();

    /**
     * 构造函数，初始化时间窗口的持续时间
     * @param windowMillis 时间窗口的持续时间，单位为毫秒
     */
    public SlidingTimeWindowCounter(long windowMillis) {
        this.windowMillis = windowMillis;
    }

    /**
     * 增加一次事件计数
     * 获取当前时间戳并将其加入到时间戳队列中，然后移除过期的时间戳
     */
    public void increment() {
        long now = System.currentTimeMillis();
        timestamps.offer(now);
        removeOldTimestamps(now);
    }

    /**
     * 获取当前时间窗口内的事件发生速率
     * 计算当前有效时间戳的数量，并根据时间窗口的长度计算速率
     * @return 当前时间窗口内的事件发生速率
     */
    public double getRate() {
        long now = System.currentTimeMillis();
        removeOldTimestamps(now);
        return timestamps.size() / (windowMillis / 1000.0);
    }

    /**
     * 私有方法，用于移除过期的时间戳
     * 遍历时间戳队列，移除所有超出当前时间窗口范围的时间戳
     * @param now 当前时间戳
     */
    private void removeOldTimestamps(long now) {
        while (!timestamps.isEmpty() &&
                timestamps.peek() < now - windowMillis) {
            timestamps.poll();
        }
    }

    /**
     * 重置计数器
     * 清空时间戳队列，将计数器状态重置为初始状态
     */
    public void reset() {
        timestamps.clear();
    }
}
