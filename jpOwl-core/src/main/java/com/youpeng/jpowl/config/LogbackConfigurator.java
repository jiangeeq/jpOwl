package com.youpeng.jpowl.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import com.youpeng.jpowl.log.JpOwlLoggingEventFilter;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class LogbackConfigurator {
    /**
     * 获取LoggerContext并添加Filter到所有Appender
     * @param filter
     */
    public static void addCustomFilterToAllAppenders(Filter<ILoggingEvent> filter) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 获取所有的 Logger
        for (Logger logger : loggerContext.getLoggerList()) {
            Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders();
            while (it.hasNext()) {
                Appender<ILoggingEvent> appender = it.next();
                appender.addFilter(filter);
            }
        }
    }

    public static void configureLogging() {
        // 创建自定义 Filter 实例
        JpOwlLoggingEventFilter customFilter = new JpOwlLoggingEventFilter();

        // 为所有 Appender 添加自定义 Filter
        addCustomFilterToAllAppenders(customFilter);
    }

}
