package com.event.management.logger;

public class CustomLoggerAdapter implements LoggerAdapter{
    @Override
    public void info(String message, Object... args) {
        System.out.printf("INFO: " + message + "%n", args);
    }

    @Override
    public void warn(String message, Object... args) {
        System.out.printf("WARN: " + message + "%n", args);
    }

    @Override
    public void error(String message, Object... args) {
        System.err.printf("ERROR: " + message + "%n", args);
    }

    @Override
    public void debug(String message, Object... args) {
        System.out.printf("DEBUG: " + message + "%n", args);
    }
}
