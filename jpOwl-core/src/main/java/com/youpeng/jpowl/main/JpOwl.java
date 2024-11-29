package com.youpeng.jpowl.main;

import com.youpeng.jpowl.annotation.MonitorPointLog;
import com.youpeng.jpowl.config.ConfigManager;
import com.youpeng.jpowl.config.LogLevel;
import com.youpeng.jpowl.core.MonitorManager;
import com.youpeng.jpowl.output.AsyncFileOutput;
import com.youpeng.jpowl.output.OutputHandler;
import com.youpeng.jpowl.trigger.Trigger;
import com.youpeng.jpowl.model.Event;
import com.youpeng.jpowl.model.Heartbeat;
import com.youpeng.jpowl.model.Metric;
import com.youpeng.jpowl.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class JpOwl {
    private static final Logger log = LoggerFactory.getLogger(JpOwl.class);
    private static MonitorManager processor;
    private static String logPrefix = "com/youpeng/jpowl";
    private static LogLevel level = LogLevel.INFO;

    static {
        OutputHandler handler = new AsyncFileOutput(ConfigManager.getProperty("log.file.path"));
        processor = new MonitorManager(handler);
    }

    public static void setLogPrefix(String prefix) {
        logPrefix = prefix;
    }

    public static void setLogLevel(LogLevel level) {
        ConfigManager.setLogLevel(level);
    }

    public static void registerTrigger(Trigger trigger) {
        processor.addTrigger(trigger);
    }

    private static LogLevel determineLogLevel(long duration, int failureCount, long dataSize) {
        if (failureCount > 10 || duration > 5000 || dataSize > 1000000) {
            return LogLevel.ERROR;
        } else if (duration > 3000 || failureCount > 5) {
            return LogLevel.WARN;
        } else if (duration > 1000) {
            return LogLevel.INFO;
        } else {
            return LogLevel.DEBUG;
        }
    }

    public static void logTransaction(String name, long duration, int failureCount, long dataSize) {
        LogLevel level = determineLogLevel(duration, failureCount, dataSize);
        Transaction transaction = new Transaction(name, duration, logPrefix);
        processor.log(transaction, level);
    }

    public static void logEvent(String name, String detail) {
        Event event = new Event(name, detail, logPrefix);
        processor.log(event, level);
    }

    public static void logHeartbeat(String name, String status) {
        Heartbeat heartbeat = new Heartbeat(name, status, logPrefix);
        processor.log(heartbeat, level);
    }

    public static void logMetric(String name, double value) {
        Metric metric = new Metric(name, value, logPrefix);
        processor.log(metric, level);
    }

    @MonitorPointLog
    public static void log1() {
        for (int i = 1; i <= 8; i++) {
            log.debug("This is a debug message number {}", i);
        }
    }

    public static void log2() {
        for (int i = 1; i <= 8; i++) {
            log.debug("不能受到影响的日志 {}", i);
        }
    }
}