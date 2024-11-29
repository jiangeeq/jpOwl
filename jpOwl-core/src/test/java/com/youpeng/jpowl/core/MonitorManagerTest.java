package com.youpeng.jpowl.core;

import com.youpeng.jpowl.config.ConfigManager;
import com.youpeng.jpowl.config.LogLevel;
import com.youpeng.jpowl.output.OutputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAsync
public class MonitorManagerTest {
    @Autowired
    private MonitorManager monitorManager;

    private OutputHandler mockOutputHandler;
    private ConfigManager mockConfigManager;

    @BeforeEach
    public void setUp() {
        mockOutputHandler = Mockito.mock(OutputHandler.class);
        mockConfigManager = Mockito.mock(ConfigManager.class);
        monitorManager = new MonitorManager(mockOutputHandler);
    }

    @Test
    public void testLogAsync() {
        Mockito.when(mockConfigManager.getLogLevel()).thenReturn(LogLevel.valueOf("INFO"));

        monitorManager.log("Test message");

        // 验证logAsync方法是否被调用
        Mockito.verify(mockOutputHandler, Mockito.timeout(1000)).write(ArgumentMatchers.anyString());
    }

    @Test
    public void testLog() {
        Mockito.when(mockConfigManager.getLogLevel()).thenReturn(LogLevel.valueOf("INFO"));

        monitorManager.log("Test message", LogLevel.INFO);

        // 验证log方法是否被调用
        Mockito.verify(mockOutputHandler, Mockito.timeout(1000)).write(ArgumentMatchers.anyString());
    }
}