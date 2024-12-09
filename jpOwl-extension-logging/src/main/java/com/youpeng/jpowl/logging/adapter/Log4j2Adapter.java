package com.youpeng.jpowl.logging.adapter;


import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * Log4j2日志框架适配器实现
 */
public class Log4j2Adapter implements LoggerAdapter {
    private final LoggerContext context;

    public Log4j2Adapter() {
        this.context = (LoggerContext)LogManager.getContext(false);
    }

    @Override
    public boolean isSupported() {
        try {
            Class.forName("org.apache.logging.log4j.core.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void log(LogEvent event) {
        Logger logger = context.getLogger(event.getLoggerName());
        org.apache.logging.log4j.Level level = convertLevel(event.getLevel());
        logger.log(level, formatMessage(event));
    }

    @Override
    public void setLevel(String loggerName, LogLevel level) {
        // 获取Log4j2的Configuration
        Configuration configuration = context.getConfiguration();
        // 获取或创建LoggerConfig
        LoggerConfig loggerConfig = configuration.getLoggerConfig(loggerName);
        // 如果logger名称不存在，创建新的LoggerConfig
        if (!loggerName.equals(loggerConfig.getName())) {
            loggerConfig = new LoggerConfig(loggerName, convertLevel(level), true);
            configuration.addLogger(loggerName, loggerConfig);
        } else {
            loggerConfig.setLevel(convertLevel(level));
        }
        // 更新配置
        context.updateLoggers();
    }

    @Override
    public LogLevel getLevel(String loggerName) {
        LoggerConfig loggerConfig = context.getConfiguration().getLoggerConfig(loggerName);
        return convertToLogLevel(loggerConfig.getLevel());
    }

    /**
     * 将LogLevel转换为Log4j2的Level
     */
    private Level convertLevel(LogLevel level) {
        switch (level) {
            case TRACE:
                return Level.TRACE;
            case DEBUG:
                return Level.DEBUG;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARN;
            case ERROR:
                return Level.ERROR;
            default:
                return Level.INFO;
        }
    }

    /**
     * 将Log4j2的Level转换为LogLevel
     * {@link org.apache.logging.log4j.spi.StandardLevel#intLevel()}
     */
    private LogLevel convertToLogLevel(Level level) {
        if (level == null) return LogLevel.INFO;
        switch (level.intLevel()) {
            case 600 : return  LogLevel.TRACE;  // Level.TRACE 的 intLevel
            case 500 : return  LogLevel.DEBUG;  // Level.DEBUG 的 intLevel
            case 400 : return  LogLevel.INFO;   // Level.INFO 的 intLevel
            case 300 : return  LogLevel.WARN;   // Level.WARN 的 intLevel
            case 200 : return  LogLevel.ERROR;  // Level.ERROR 的 intLevel
            default : return  LogLevel.INFO;
        }
    }
}