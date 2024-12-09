package com.youpeng.jpowl.core.spi;

import com.youpeng.jpowl.core.api.AlertTrigger;
import java.util.Map;

public interface AlertTriggerProvider {
    AlertTrigger createTrigger(Map<String, Object> properties);
    String getType();
} 