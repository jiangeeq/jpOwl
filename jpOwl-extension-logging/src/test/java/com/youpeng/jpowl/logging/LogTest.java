package com.youpeng.jpowl.logging;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    void testLogging() {
        logger.info("Test log message");
        logger.debug("Debug message");
        logger.error("Error message");
    }
} 