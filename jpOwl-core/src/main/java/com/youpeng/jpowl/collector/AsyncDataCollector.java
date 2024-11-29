package com.youpeng.jpowl.collector;

import com.youpeng.jpowl.io.NioLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// 使用异步数据采集
// 异步数据采集可以使用Java并发包中的ExecutorService来实现。
public class AsyncDataCollector {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final NioLogger nioLogger;

    public AsyncDataCollector(NioLogger nioLogger) {
        this.nioLogger = nioLogger;
    }

    public void collectData(String data) {
        executorService.submit(() -> {
            try {
                // 模拟数据处理
                Thread.sleep(100);
                nioLogger.log(data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        nioLogger.shutdown();
    }
}
