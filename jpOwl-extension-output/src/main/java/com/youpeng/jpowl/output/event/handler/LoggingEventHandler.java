package com.youpeng.jpowl.output.event.handler;

import com.youpeng.jpowl.output.event.OutputSourceEvent;
import com.youpeng.jpowl.output.event.OutputSourceEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志事件处理器
 * 将输出源事件记录到日志中
 */
public class LoggingEventHandler implements OutputSourceEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingEventHandler.class);

    @Override
    public void handleEvent(OutputSourceEvent event) {
        switch (event.getType()) {
            case STARTED:
                logger.info("Output source {} started: {}", event.getSourceType(), event.getDetails());
                break;
            case STOPPED:
                logger.info("Output source {} stopped: {}", event.getSourceType(), event.getDetails());
                break;
            case WRITE_SUCCESS:
                logger.debug("Write success for {}: {}", event.getSourceType(), event.getDetails());
                break;
            case WRITE_FAILED:
                logger.error("Write failed for {}: {}", event.getSourceType(), event.getDetails(), event.getThrowable());
                break;
            case ERROR:
                logger.error("Error in output source {}: {}", event.getSourceType(), event.getDetails(), event.getThrowable());
                break;
            default:
                logger.warn("Unknown event type {} for {}", event.getType(), event.getSourceType());
        }
    }
} 