package com.youpeng.jpowl.context;

import java.util.HashMap;
import java.util.Map;

public class ThreadContext {
    private static ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    public static void put(String key, Object value) {
        threadLocal.get().put(key, value);
    }

    public static Object get(String key) {
        return threadLocal.get().get(key);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
