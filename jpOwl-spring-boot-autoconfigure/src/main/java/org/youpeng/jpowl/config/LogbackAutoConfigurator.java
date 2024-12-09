package org.youpeng.jpowl.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.Appender;
import com.youpeng.jpowl.log.JpOwlLoggingEventFilter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Configuration
public class LogbackAutoConfigurator {
    // 通过监听ApplicationReadyEvent事件来确保在应用启动后添加Filter。
    @EventListener(ApplicationReadyEvent.class)
    public void configureLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        JpOwlLoggingEventFilter customFilter = new JpOwlLoggingEventFilter();
        addCustomFilterToAllAppenders(loggerContext, customFilter);
    }

    private void addCustomFilterToAllAppenders(LoggerContext loggerContext, JpOwlLoggingEventFilter filter) {
        for (Logger logger : loggerContext.getLoggerList()) {
            Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders();
            while (it.hasNext()) {
                Appender<ILoggingEvent> appender = it.next();
                appender.addFilter(filter);
            }
        }
    }

//    @PostConstruct
    public void addCustomTurboFilter() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        TurboFilter customTurboFilter = new CustomTurboFilter();

        loggerContext.addTurboFilter(customTurboFilter);
    }
}
