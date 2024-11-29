package com.youpeng.jpowl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
public class JpOwlStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpOwlStarterApplication.class, args);
    }
}
