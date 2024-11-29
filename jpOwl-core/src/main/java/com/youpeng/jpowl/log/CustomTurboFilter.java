package com.youpeng.jpowl.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * 通过实现自定义的TurboFilter，你可以在日志事件传递到Appender之前进行全局级别的过滤决策。
 * 例如，可以根据日志消息的内容、日志级别、日志来源等信息决定是否允许日志事件继续传递。
 */
public class CustomTurboFilter extends TurboFilter {
    // 创建或获取一个 Marker
    private static final Marker SECURITY_MARKER = MarkerFactory.getMarker("SECURITY");
    private static final Marker PERFORMANCE_MARKER = MarkerFactory.getMarker("PERFORMANCE");
    // 嵌套 Marker 用于更复杂的标记和分类策略。
    // Marker parentMarker = MarkerFactory.getMarker("PARENT");
    //Marker childMarker = MarkerFactory.getMarker("CHILD");
    //parentMarker.add(childMarker);
    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        // 通过 Marker 进行日志过滤
        if (marker != null && marker.contains("IMPORTANT")) {
            // 如果 Marker 包含 "IMPORTANT"，则总是记录
            return FilterReply.ACCEPT;
        }

        // 根据日志级别进行过滤
        if (level.isGreaterOrEqual(Level.ERROR)) {
            // 如果日志级别为 ERROR 或更高，则记录
            return FilterReply.ACCEPT;
        }

        // 根据日志消息内容进行过滤
        if (format != null && format.contains("specific keyword")) {
            // 如果日志消息包含特定关键字，则拒绝
            return FilterReply.DENY;
        }

        // 默认情况下继续处理
        return FilterReply.NEUTRAL;
    }
}
