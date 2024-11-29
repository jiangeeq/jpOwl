package com.youpeng.jpowl.aspect;

import com.youpeng.jpowl.annotation.Monitor;
import com.youpeng.jpowl.config.LogLevel;
import com.youpeng.jpowl.core.MonitorManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
public class MonitorAspectTest {
    @Autowired
    private MonitorAspect monitorAspect;

    private MonitorManager mockMonitorManager;
    private ProceedingJoinPoint mockJoinPoint;
    private MethodSignature mockMethodSignature;


    @BeforeEach
    public void setUp() {
        mockMonitorManager = Mockito.mock(MonitorManager.class);
        monitorAspect = new MonitorAspect();

        mockJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        mockMethodSignature = Mockito.mock(MethodSignature.class);

        Mockito.when(mockJoinPoint.getSignature()).thenReturn(mockMethodSignature);
        Mockito.when(mockMethodSignature.getName()).thenReturn("exampleMethod");
    }

    @Test
    public void testAround() throws Throwable {
        Monitor monitor = Mockito.mock(Monitor.class);
        Mockito.when(monitor.logLevel()).thenReturn(LogLevel.INFO);
        Mockito.when(mockJoinPoint.proceed()).thenReturn(null);

        monitorAspect.around(mockJoinPoint, monitor);

        Mockito.verify(mockMonitorManager, Mockito.times(1)).log(ArgumentMatchers.anyString());
    }

    @Test
    public void testAroundWithException() throws Throwable {
        Monitor monitor = Mockito.mock(Monitor.class);
        Mockito.when(monitor.logLevel()).thenReturn(LogLevel.INFO);
        Mockito.when(mockJoinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));

        try {
            monitorAspect.around(mockJoinPoint, monitor);
        } catch (Throwable throwable) {
            // Expected exception
        }

        Mockito.verify(mockMonitorManager, Mockito.times(1)).log(ArgumentMatchers.anyString());
    }
}