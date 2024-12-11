package com.event.management.logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoggerFactory {

    @Value("${logging.framework:slf4j}")
    private String loggingFramework;

    public LoggerAdapter createLoggerAdapter() {
        switch (loggingFramework.toLowerCase()) {
            case "log4j":
                return new Log4jAdapter();
            case "custom":
                return new CustomLoggerAdapter();
            case "slf4j":
            default:
                return new Slf4jAdapter();
        }
    }
}

