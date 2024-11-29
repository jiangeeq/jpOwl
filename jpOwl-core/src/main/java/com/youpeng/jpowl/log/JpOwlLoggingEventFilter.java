package com.youpeng.jpowl.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.lang.reflect.Field;

public class JpOwlLoggingEventFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        // 在这里对 LoggingEvent 进行修改
        if (event.getLevel().isGreaterOrEqual(Level.INFO)) {
            // 修改日志事件，例如添加前缀
            String modifiedMessage = "[MODIFIED] " + event.getFormattedMessage();
            // 反射方式修改 message
            try {
                Field field = event.getClass().getDeclaredField("level");
                field.setAccessible(true);
                field.set(event, Level.ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 根据修改后的level属性决定是否跳过appender
            if (event.getLevel().equals(Level.DEBUG)) {
                return FilterReply.DENY; // 跳过Appender
            }
        }

        // 继续处理日志事件
        return FilterReply.NEUTRAL;
    }
}
