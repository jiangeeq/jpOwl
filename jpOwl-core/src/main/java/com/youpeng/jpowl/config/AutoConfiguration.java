package com.youpeng.jpowl.config;

import com.youpeng.jpowl.core.MonitorManager;
import com.youpeng.jpowl.io.NioLogger;
import com.youpeng.jpowl.output.AsyncFileOutput;
import com.youpeng.jpowl.output.OutputHandler;
import com.youpeng.jpowl.trigger.Trigger;
import com.youpeng.jpowl.collector.AsyncDataCollector;
import com.youpeng.jpowl.trigger.AlertTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AutoConfiguration  {
    @Value("jpowl.log.file.path")
    private String filePath;

    @Bean
    public ConfigManager configManager() {
        return new ConfigManager();
    }

    @Bean
    public MonitorManager monitorManager(OutputHandler outputHandler, ConfigManager configManager) {
        return MonitorManager.getInstance(outputHandler, configManager);
    }

    @Bean
    public OutputHandler fileOutputHandler() {
        return new AsyncFileOutput("e:/");
    }

    @Bean
    public Trigger alertTrigger() {
        return new AlertTrigger("email");
    }

    @Bean
    public NioLogger nioLogger() throws IOException {
        return new NioLogger(filePath);
    }

    @Bean
    public AsyncDataCollector asyncDataCollector(NioLogger nioLogger) {
        return new AsyncDataCollector(nioLogger);
    }
}
