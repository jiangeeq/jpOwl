package com.youpeng.jpowl.example;

import com.youpeng.jpowl.collector.AsyncDataCollector;
import com.youpeng.jpowl.core.MonitorManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExampleServiceTest {

    @InjectMocks
    private ExampleService exampleService;

    @Mock
    private MonitorManager monitorManager;
    @Mock
    private AsyncDataCollector asyncDataCollector;

    public ExampleServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExampleMethod() {
        exampleService.demo1();

        Mockito.verify(monitorManager, Mockito.timeout(1000)).log( ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    public void testProcessData() {
        String data = "test data";
        exampleService.processData(data);

        Mockito.verify(asyncDataCollector, Mockito.times(1)).collectData(data);
    }
}