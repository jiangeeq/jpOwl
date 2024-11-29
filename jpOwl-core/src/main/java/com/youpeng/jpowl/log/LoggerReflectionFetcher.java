package com.youpeng.jpowl.log;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class LoggerReflectionFetcher {
    // 反射获取所有这些类中的Logger实例
    public List<Logger> getLoggerList() throws IllegalAccessException {
        // 获取当前包中的所有类
        Reflections reflections = new Reflections("com.example", new SubTypesScanner(false));
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        List<Logger> loggers = new ArrayList<>();
        for (Class<?> clazz : allClasses) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Logger.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Logger logger = (Logger) field.get(null);
                    loggers.add(logger);
                    System.out.println("Found logger in class: " + clazz.getName());
                }
            }
        }

        // 输出所有找到的Logger
        for (Logger logger : loggers) {
            System.out.println("Logger Name: " + logger.getName());
        }

        return loggers;
    }
}
