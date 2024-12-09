package com.youpeng.jpowl.core;

import com.youpeng.jpowl.config.LogLevel;
import com.youpeng.jpowl.model.*;
import com.youpeng.jpowl.output.OutputHandler;
import com.youpeng.jpowl.trigger.Trigger;
import com.youpeng.jpowl.aggregator.MetricAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MonitorManager {
    private static MonitorManager instance;

    private BlockingQueue<MonitorModel> queue = new LinkedBlockingQueue<>();
    private List<Trigger> triggers = new ArrayList<>();
    private OutputHandler outputHandler;

    private final BlockingQueue<Transaction> transactionQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Heartbeat> heartbeatQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Metric> metricQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();


    @Autowired
    private ConfigManager configManager;
    @Value("${jpowl.log.prefix:default}")
    private String logPrefix;

    public MonitorManager(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
        this.configManager = configManager;
        new Thread(this::processQueue).start();
    }

    public static MonitorManager getInstance(OutputHandler outputHandler, ConfigManager configManager) {
        if (instance == null) {
            instance = new MonitorManager(outputHandler);
        }
        return instance;
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void log(MonitorModel model, LogLevel level) {
        logAsync(model, level);
    }

    public void log(String message, LogLevel level) {
        MonitorModel model = new MonitorModel(message) {
            @Override
            public String serialize() {
                return "";
            }
        };
        log(model, level);
    }

    public void log(String message) {
        log(message, LogLevel.INFO);
    }


    // 以支持异步日志记录，并添加方法以异步编程方式记录监控信息。
    @Async
    protected void logAsync(MonitorModel model, LogLevel level) {
        if (level.ordinal() >= ConfigManager.getLogLevel().ordinal()) {
            String prefixedMessage = logPrefix + ": " + model.getMessage();
            model.setMessage(prefixedMessage);
            queue.offer(model);
        }
    }


    private void processQueue() {
        while (!queue.isEmpty()) {
            try {
                MonitorModel model = queue.take();
                outputHandler.write(model.serialize());
                aggregateMetrics(model);
                for (Trigger trigger : triggers) {
                    trigger.execute(model);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void aggregateMetrics(MonitorModel model) {
        String key = model.getMessage();
        if (model instanceof Transaction) {
            Transaction transaction = (Transaction) model;
            MetricAggregator.incrementFailureCount(key);
            if (transaction.getDuration() > 5000) {
                MetricAggregator.incrementFailureCount(key);
            }
        }
    }

    private void startLogConsumers() {
        executorService.submit(() -> {
            while (true) {
                try {
                    Transaction transaction = transactionQueue.take();
                    // 将事务记录到日志或数据库中
                    System.out.println(transaction);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        executorService.submit(() -> {
            while (true) {
                try {
                    Event event = eventQueue.take();
                    // 将事件记录到日志或数据库中
                    System.out.println(event);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        executorService.submit(() -> {
            while (true) {
                try {
                    Heartbeat heartbeat = heartbeatQueue.take();
                    // 将心跳记录到日志或数据库中
                    System.out.println(heartbeat);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        executorService.submit(() -> {
            while (true) {
                try {
                    Metric metric = metricQueue.take();
                    // 将指标记录到日志或数据库中
                    System.out.println(metric);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void logTransaction(Transaction transaction) {
        transactionQueue.offer(transaction);
    }

    public void logEvent(Event event) {
        eventQueue.offer(event);
    }

    public void logHeartbeat(Heartbeat heartbeat) {
        heartbeatQueue.offer(heartbeat);
    }

    public void logMetric(Metric metric) {
        metricQueue.offer(metric);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
