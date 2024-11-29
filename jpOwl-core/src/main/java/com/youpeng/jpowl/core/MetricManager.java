package com.youpeng.jpowl.core;

import com.youpeng.jpowl.model.Metric;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MetricManager {

    private final ConcurrentMap<String, Metric> metrics = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MetricManager() {
        startMetricProcessor();
    }

    public void recordMetric(String name, double value) {
        metrics.computeIfAbsent(name, Metric::new).record(value);
    }

    private void startMetricProcessor() {
        scheduler.scheduleAtFixedRate(this::processMetrics, 0, 1, TimeUnit.MINUTES);
    }

    private void processMetrics() {
        metrics.forEach((name, metric) -> {
            // 在这里处理每分钟的统计数据，例如记录到日志或发送到监控系统
            System.out.println("Processing metric: " + metric);
            // 处理完成后重置metric数据
            metric.reset();
        });
    }

    public void shutdown() {
        scheduler.shutdown();
    }

//    public static void main(String[] args) {
//        MetricManager metricManager = new MetricManager();
//        metricManager.recordMetric("BusinessMetric", 10.0);
//        metricManager.recordMetric("BusinessMetric", 20.0);
//        // 模拟主线程工作
//        try {
//            Thread.sleep(60000); // 1分钟
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        metricManager.shutdown();
//    }
}