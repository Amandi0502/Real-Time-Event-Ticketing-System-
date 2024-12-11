package com.event.management.logger;

import org.springframework.stereotype.Component;

@Component
public interface LoggerAdapter {
    void info(String message, Object... args);  // General info message logging with variable arguments
    void warn(String message, Object... args);  // Warn message logging with variable arguments
    void error(String message, Object... args);  // Error message logging with variable arguments
    void debug(String message, Object... args);  // Debug message logging with variable arguments
}
