package com.event.management.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jAdapter implements LoggerAdapter{

    private final Logger logger = LoggerFactory.getLogger(Slf4jAdapter.class);

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }
}
