package com.youpeng.jpowl.example;

import com.youpeng.jpowl.annotation.Monitor;
import com.youpeng.jpowl.collector.AsyncDataCollector;
import com.youpeng.jpowl.config.LogLevel;
import com.youpeng.jpowl.core.MonitorManager;
import com.youpeng.jpowl.model.MonitorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    @Autowired
    private MonitorManager monitorManager;
    @Autowired
    private AsyncDataCollector asyncDataCollector;

    @Monitor(logLevel = LogLevel.INFO)
     public void demo1(){
         System.out.println("demo1()");
     }

    /**
     * 在代码中直接调用MonitorManager的log方法进行监控。
     */
    public void demo2(){
        long startTime = System.currentTimeMillis();
        System.out.println("demo2()");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            long endTime = System.currentTimeMillis();
            monitorManager.log(new MonitorModel("abc") {
                @Override
                public String serialize() {
                    return "";
                }
            }, LogLevel.INFO);
        }
    }

    public void processData(String data) {
        // 处理数据
        asyncDataCollector.collectData(data);
    }
}
