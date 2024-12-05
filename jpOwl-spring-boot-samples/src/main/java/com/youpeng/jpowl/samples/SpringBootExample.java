package com.youpeng.jpowl.samples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringBootExample {
    @Autowired
    private JpOwlTemplate jpOwlTemplate;
    
    @Monitor
    public void doSomething() {
        // 业务逻辑
        jpOwlTemplate.logTransaction("business", 200);
    }
} 