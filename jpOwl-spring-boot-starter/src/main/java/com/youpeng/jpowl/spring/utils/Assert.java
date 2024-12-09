package com.youpeng.jpowl.spring.utils;

import org.springframework.lang.Nullable;

/**
 * 断言工具类
 */
public abstract class Assert {
    
    /**
     * 断言对象不为空
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
} 