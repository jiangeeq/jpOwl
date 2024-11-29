package com.youpeng.jpowl;


import com.youpeng.jpowl.main.JpOwl;
import com.youpeng.jpowl.aspect.LoggerAspect;
import com.youpeng.jpowl.config.LogLevel;
import com.youpeng.jpowl.trigger.AlertTrigger;
import com.youpeng.jpowl.trigger.EmailAlertTrigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest     // 启动整个 Spring 应用程序上下文。
public class JpOwlTest {
    private JpOwl jpOwl;
    private static final Logger logger = LoggerFactory.getLogger(JpOwlTest.class);

    // Spring 测试框架会自动发现使用 @SpyBean 注解的字段，并在 Spring 容器中注入相应的 spy 对象。
    @SpyBean
    private LoggerAspect loggerAspect;

    @BeforeEach
    public void setUp() {
        // Initialize the real instance of JpOwl
        JpOwl jpOwl = new JpOwl();
    }


    @Test
    public void testLogging() {
        jpOwl.setLogPrefix("[MyApp]");
        jpOwl.setLogLevel(LogLevel.DEBUG);

        EmailAlertTrigger emailTrigger = new EmailAlertTrigger();
        jpOwl.registerTrigger(emailTrigger);
        AlertTrigger smsTrigger = new AlertTrigger("sms");
        jpOwl.registerTrigger(smsTrigger);


        jpOwl.logTransaction("DatabaseCall", 123, 0, 500);
        // Should trigger email alert
        jpOwl.logTransaction("RemoteService", 5000, 1, 2000000);
        jpOwl.logEvent("SystemError", "NullPointerException");
        jpOwl.logHeartbeat("ServerStatus", "OK");
        jpOwl.logMetric("ResponseTime", 256.78);
    }

    @Test
    public void test2(){
        jpOwl.log2();
        jpOwl.log1();
        jpOwl.log2();
    }

    @Test
    public void testLoggerAspect() {
        logger.debug("Test debug message");
        logger.info("Test info message");
        logger.error("Test error message");

        // 验证切面方法是否被调用
        Mockito.verify(loggerAspect, Mockito.times(3)).logBefore();
    }
}

